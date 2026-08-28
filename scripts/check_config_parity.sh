#!/usr/bin/env bash
set -euo pipefail

FABRIC_CONFIG='Fabric/src/main/java/vazkii/psi/fabric/FiberPsiConfig.java'
NEOFORGE_CONFIG='NeoForge/src/main/java/vazkii/psi/neoforge/NeoForgePsiConfig.java'

assert_once() {
	local file="$1"
	local text="$2"
	local count
	count="$(grep -Fc -- "${text}" "${file}" || true)"
	[[ "${count}" -eq 1 ]] || {
		echo "Expected one occurrence in ${file}: ${text}" >&2
		exit 1
	}
}

boolean_settings=(
	'client|useShaders|true'
	'client|psiBarOnRight|true'
	'client|contextSensitiveBar|true'
	'client|pauseGameInProgrammer|true'
	'client|changeGridCoordinatesToLetterNumber|false'
	'common|magiPsiClientSide|false'
)

for setting in "${boolean_settings[@]}"; do
	IFS='|' read -r scope name default_value <<<"${setting}"
	assert_once "${FABRIC_CONFIG}" ".beginValue(\"${name}\", BOOLEAN, ${default_value})"
	assert_once "${NEOFORGE_CONFIG}" ".define(\"${scope}.${name}\", ${default_value});"
done

integer_settings=(
	'client|maxPsiBarScale|PSI_BAR_SCALE|3|1|5'
	'common|spellCacheSize|SPELL_CACHE_SIZE|200|0|Integer.MAX_VALUE'
	'common|cadHarvestLevel|CAD_HARVEST_LEVEL|3|0|255'
)

for setting in "${integer_settings[@]}"; do
	IFS='|' read -r scope name fabric_type default_value minimum maximum <<<"${setting}"
	assert_once "${FABRIC_CONFIG}" ".beginValue(\"${name}\", ${fabric_type}, ${default_value})"
	assert_once "${FABRIC_CONFIG}" "${fabric_type} = INTEGER.withMinimum(${minimum}).withMaximum(${maximum});"
	assert_once "${NEOFORGE_CONFIG}" ".defineInRange(\"${scope}.${name}\", ${default_value}, ${minimum}, ${maximum});"
done

[[ "$(grep -Fc '.beginValue(' "${FABRIC_CONFIG}")" -eq 9 ]] || {
	echo 'Fabric config contains an untracked or missing setting' >&2
	exit 1
}
[[ "$(grep -Ec '\.define(InRange)?\(' "${NEOFORGE_CONFIG}")" -eq 9 ]] || {
	echo 'NeoForge config contains an untracked or missing setting' >&2
	exit 1
}

assert_once "${FABRIC_CONFIG}" 'configDirectory.resolve("psi-common.json5")'
assert_once "${FABRIC_CONFIG}" 'configDirectory.resolve("psi-client.json5")'
assert_once "${FABRIC_CONFIG}" 'getEnvironmentType() == EnvType.CLIENT'
assert_once "${NEOFORGE_CONFIG}" 'container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);'
assert_once "${NEOFORGE_CONFIG}" 'container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);'
assert_once "${NEOFORGE_CONFIG}" 'if(dist.isClient())'

echo 'Fabric and NeoForge config schemas match'
