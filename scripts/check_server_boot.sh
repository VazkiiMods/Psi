#!/usr/bin/env bash
set -euo pipefail

SERVER_PID=''
SERVER_LOG=''

cleanup() {
	if [[ -n "${SERVER_PID}" ]] && kill -0 "${SERVER_PID}" 2>/dev/null; then
		kill -INT -- "-${SERVER_PID}" 2>/dev/null || true
		for _ in {1..10}; do
			kill -0 "${SERVER_PID}" 2>/dev/null || break
			sleep 0.1
		done
		kill -TERM -- "-${SERVER_PID}" 2>/dev/null || true
	fi
	if [[ -n "${SERVER_LOG}" ]]; then
		rm -f "${SERVER_LOG}"
	fi
}
trap cleanup EXIT INT TERM

boot_server() {
	local project="$1"
	local run_dir="$2"
	SERVER_LOG="$(mktemp)"
	mkdir -p "${run_dir}"
	printf 'eula=true\n' > "${run_dir}/eula.txt"

	setsid ./gradlew ":${project}:runServer" --console=plain >"${SERVER_LOG}" 2>&1 &
	SERVER_PID=$!

	for _ in {1..120}; do
		if grep -Fq 'Done (' "${SERVER_LOG}"; then
			echo "${project} dedicated server reached Done"
			cleanup
			wait "${SERVER_PID}" 2>/dev/null || true
			SERVER_PID=''
			SERVER_LOG=''
			return
		fi

		if ! kill -0 "${SERVER_PID}" 2>/dev/null; then
			cat "${SERVER_LOG}"
			echo "${project} dedicated server exited before reaching Done" >&2
			exit 1
		fi

		sleep 1
	done

	cat "${SERVER_LOG}"
	echo "${project} dedicated server did not reach Done within 120 seconds" >&2
	exit 1
}

boot_server Fabric runs/fabric
boot_server NeoForge runs/server
