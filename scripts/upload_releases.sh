#!/usr/bin/env bash
set -euo pipefail

# Remove 'refs/tags/' from front
TAGNAME="${GIT_REF/#refs\/tags\/}"

# Remove 'release-' from front
VERSION="${TAGNAME/#release-}"
MC_VERSION=$(echo "${VERSION}" | cut -d '-' -f 1)

function release_github() {
	echo >&2 'Creating GitHub Release'
	local GH_RELEASE_RESPONSE
	GH_RELEASE_RESPONSE="$(gh api \
	   --method POST \
	   -H "Accept: application/vnd.github+json" \
	   -H "X-GitHub-Api-Version: 2022-11-28" \
	   /repos/VazkiiMods/Psi/releases \
	   -f tag_name="${TAGNAME}")"
	GH_RELEASE_PAGE=$(echo "$GH_RELEASE_RESPONSE" | jq -r .html_url)

	echo >&2 'Uploading Forge Jar and Signature to GitHub'
	gh release upload "${TAGNAME}" "${FORGE_JAR}#Forge Jar"
	gh release upload "${TAGNAME}" "${FORGE_JAR}.asc#Forge Signature"
}

function release_modrinth() {
	echo >&2 'Uploading Forge Jar to Modrinth'
	local MODRINTH_FORGE_SPEC
	MODRINTH_FORGE_SPEC=$(cat <<EOF
{
	"dependencies": [
			{
			"project_id": "nU0bVIaL",
			"dependency_type": "required"
	}],
	"version_type": "release",
	"loaders": ["forge"],
	"featured": false,
	"project_id": "pOeA0exL",
	"file_parts": [
		"jar"
	],
	"primary_file": "jar"
}
EOF
					   )

	MODRINTH_FORGE_SPEC=$(echo "${MODRINTH_FORGE_SPEC}" | \
							  jq --arg name "${VERSION}" \
								 --arg mcver "${MC_VERSION}" \
								 --arg changelog "${GH_RELEASE_PAGE}" \
								 '.name=$ARGS.named.name | .version_number=$ARGS.named.name | .game_versions=[$ARGS.named.mcver] | .changelog=$ARGS.named.changelog')
	curl 'https://api.modrinth.com/v2/version' \
		 -H "Authorization: $MODRINTH_TOKEN" \
		 -F "data=$MODRINTH_FORGE_SPEC" \
		 -F "jar=@${FORGE_JAR}" # TODO modrinth doesn't allow asc files. Remember to readd "signature" to the spec when reenabling this. \ -F "signature=@${FORGE_JAR}.asc"
}

function release_curseforge() {
	# Java versions, Loaders, and Environment tags are actually "game versions" (lmfao), as are real game versions.

	# Hardcoded from https://minecraft.curseforge.com/api/game/version-types (which is undocumented, btw)
	# I'm not betting on these changing any time soon, so hardcoding is ok
	local CURSEFORGE_JAVA_VERSION=8326 # Java 17
	local CURSEFORGE_FORGE_VERSION=7498
	local CURSEFORGE_CLIENT_VERSION=9638
	local CURSEFORGE_SERVER_VERSION=9639
	# For the Minecraft one, don't hardcode so we don't have to remember to come change this every time.
	# Each game version seems to be duplicated three times:
	# Once with type ID 1 (unused?), once with its major-version-specific type ID, and once with the type ID for "Addons" 615
	# We want the second one. Just dirtily pluck it out based on this.
	local CURSEFORGE_GAME_VERSION
	CURSEFORGE_GAME_VERSION=$(curl https://minecraft.curseforge.com/api/game/versions \
								   -H 'Accept: application/json' \
								   -H "X-Api-Token: ${CURSEFORGE_TOKEN}" | \
								  jq --arg mcver "${MC_VERSION}" \
									 'map(select(.name == $ARGS.named.mcver and .gameVersionTypeID != 1 and .gameVersionTypeID != 615)) | first | .id')

	echo >&2 'Uploading Forge Jar to CurseForge'
	local CURSEFORGE_FORGE_SPEC
	CURSEFORGE_FORGE_SPEC=$(cat <<EOF
{
    "changelogType": "text",
    "releaseType": "release"
}
EOF
						 )

	local CURSEFORGE_FORGE_GAMEVERS="[\
$CURSEFORGE_JAVA_VERSION,\
$CURSEFORGE_CLIENT_VERSION,\
$CURSEFORGE_SERVER_VERSION,\
$CURSEFORGE_FORGE_VERSION,\
$CURSEFORGE_GAME_VERSION]"

	CURSEFORGE_FORGE_SPEC=$(echo "$CURSEFORGE_FORGE_SPEC" | \
								jq --arg changelog "$GH_RELEASE_PAGE" \
								   --argjson gamevers "$CURSEFORGE_FORGE_GAMEVERS" \
								   '.gameVersions=$ARGS.named.gamevers | .changelog=$ARGS.named.changelog')
	curl 'https://minecraft.curseforge.com/api/projects/241665/upload-file' \
		 -H "X-Api-Token: $CURSEFORGE_TOKEN" \
		 -F "metadata=$CURSEFORGE_FORGE_SPEC" \
		 -F "file=@$FORGE_JAR"
	# TODO: Upload the asc as an 'Additional file'
}

release_github
release_modrinth
release_curseforge