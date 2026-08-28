#!/usr/bin/env bash
set -euo pipefail

MC_VERSION="$(awk -F '=' '$1 == "minecraft_version" { print $2 }' gradle.properties)"
MOD_VERSION="$(awk -F '=' '$1 == "mod_version" { print $2 }' gradle.properties)"
FABRIC_JAR="Fabric/build/libs/Psi-fabric-${MC_VERSION}-${MOD_VERSION}.jar"
NEOFORGE_JAR="NeoForge/build/libs/Psi-neoforge-${MC_VERSION}-${MOD_VERSION}.jar"
XPLAT_JAR="Xplat/build/libs/Psi-xplat-${MC_VERSION}-${MOD_VERSION}.jar"

fail() {
	echo "Artifact check failed: $*" >&2
	exit 1
}

for jar_file in "${FABRIC_JAR}" "${NEOFORGE_JAR}" "${XPLAT_JAR}"; do
	[[ -f "${jar_file}" ]] || fail "missing ${jar_file}"
	duplicates="$(unzip -Z1 "${jar_file}" | sort | uniq -d)"
	[[ -z "${duplicates}" ]] || fail "duplicate entries in ${jar_file}: ${duplicates}"
done

while IFS= read -r entry; do
	[[ "${entry}" == */ ]] && continue
	[[ "${entry}" == 'META-INF/MANIFEST.MF' ]] && continue

	for loader_jar in "${FABRIC_JAR}" "${NEOFORGE_JAR}"; do
		unzip -p "${loader_jar}" "${entry}" >/dev/null 2>&1 ||
			fail "${loader_jar} is missing Xplat entry ${entry}"
	done

	if [[ "${entry}" != *.class ]]; then
		xplat_hash="$(unzip -p "${XPLAT_JAR}" "${entry}" | sha256sum | cut -d' ' -f1)"
		fabric_hash="$(unzip -p "${FABRIC_JAR}" "${entry}" | sha256sum | cut -d' ' -f1)"
		neoforge_hash="$(unzip -p "${NEOFORGE_JAR}" "${entry}" | sha256sum | cut -d' ' -f1)"
		[[ "${xplat_hash}" == "${fabric_hash}" ]] ||
			fail "Fabric changed Xplat resource ${entry}"
		[[ "${xplat_hash}" == "${neoforge_hash}" ]] ||
			fail "NeoForge changed Xplat resource ${entry}"
	fi
done < <(unzip -Z1 "${XPLAT_JAR}")

unzip -p "${XPLAT_JAR}" vazkii/psi/client/gui/GuiProgrammer.class >/dev/null 2>&1 ||
	fail 'Psi-xplat does not contain client bytecode'

mapfile -t fabric_services < <(unzip -Z1 "${FABRIC_JAR}" | sed -n '/^META-INF\/services\/./p' | sort)
mapfile -t neoforge_services < <(unzip -Z1 "${NEOFORGE_JAR}" | sed -n '/^META-INF\/services\/./p' | sort)
[[ "${fabric_services[*]}" == "${neoforge_services[*]}" ]] || fail 'loader service descriptor sets differ'
[[ "${#fabric_services[@]}" -eq 11 ]] || fail "expected 11 platform services, found ${#fabric_services[@]}"

for service in "${fabric_services[@]}"; do
	fabric_provider="$(unzip -p "${FABRIC_JAR}" "${service}")"
	neoforge_provider="$(unzip -p "${NEOFORGE_JAR}" "${service}")"
	[[ "${fabric_provider}" == vazkii.psi.fabric.platform.* ]] || fail "invalid Fabric provider for ${service}"
	[[ "${neoforge_provider}" == vazkii.psi.neoforge.platform.* ]] || fail "invalid NeoForge provider for ${service}"
done

fabric_entries="$(unzip -Z1 "${FABRIC_JAR}")"
neoforge_entries="$(unzip -Z1 "${NEOFORGE_JAR}")"
[[ "$(grep -Fxc 'psi-xplat-client.mixins.json' <<<"${fabric_entries}")" -eq 1 ]] || fail 'Fabric Xplat mixin count is not one'
[[ "$(grep -Fxc 'psi-fabric.mixins.json' <<<"${fabric_entries}")" -eq 1 ]] || fail 'Fabric loader mixin count is not one'
[[ "$(grep -Fxc 'psi-xplat-client.mixins.json' <<<"${neoforge_entries}")" -eq 1 ]] || fail 'NeoForge Xplat mixin count is not one'
[[ "$(grep -Fxc 'psi-neoforge.mixins.json' <<<"${neoforge_entries}")" -eq 1 ]] || fail 'NeoForge loader mixin count is not one'

[[ "$(grep -Ec '^META-INF/jars/fiber-[^/]+\.jar$' <<<"${fabric_entries}")" -eq 1 ]] || fail 'Fabric Fiber nesting is invalid'
[[ "$(grep -Ec '^META-INF/jars/sable-companion-fabric-[^/]+\.jar$' <<<"${fabric_entries}")" -eq 1 ]] || fail 'Fabric Sable nesting is invalid'
[[ "$(grep -Ec '^META-INF/jarjar/sable-companion-common-[^/]+\.jar$' <<<"${neoforge_entries}")" -eq 1 ]] || fail 'NeoForge Sable nesting is invalid'

fabric_metadata="$(unzip -p "${FABRIC_JAR}" fabric.mod.json)"
neoforge_metadata="$(unzip -p "${NEOFORGE_JAR}" META-INF/neoforge.mods.toml)"
grep -Fq '"minecraft": "1.21.1"' <<<"${fabric_metadata}" || fail 'Fabric Minecraft bound is not exact'
grep -Fq 'loaderVersion = "[4,)"' <<<"${neoforge_metadata}" || fail 'NeoForge loader bound is stale'
grep -Fq 'versionRange = "[21.1.207,)"' <<<"${neoforge_metadata}" || fail 'NeoForge version bound is stale'
grep -Fq 'versionRange = "1.21.1"' <<<"${neoforge_metadata}" || fail 'NeoForge Minecraft bound is not exact'

echo 'Fabric, NeoForge, and Xplat artifacts passed structural checks'
