#!/bin/bash

if [ -z "$1" ]; then
	printf "Usage: $0 <lang> [<output file>]\n"
	exit 11
fi
lang=$1
patchouli_lang=en_us
botania_path="$(dirname "$0")"
JQ='jq'
FIND='find'
output=${2-mappings-$lang.json}

# Edit above lines to configure-- defaults should work, though.
# This script cuts-and-pastes Botania's data files into a file (default 'mappings.json').
# That file should then get passed to 'convert.pl' to find all unmapped Patchi items.
# Unmapped items should either get thrown into the "ignore" array,
# or mapped under "names" to a botania lang-entry name.
# Requires 'jq' and 'find'.

patchouli_path=src/main/resources/data/botania/patchouli_books/lexicon/$patchouli_lang/entries
lang_file=src/main/resources/assets/botania/lang/$lang.json
lang_filter='to_entries
	| map(select(.key
		|   contains("botania.entry.")
		or  contains("botania.challenge.")
		and (contains(".desc") | not))
	| {(.value): .key})
	|add'

cd "$botania_path"
printf >&2 "Writing output to $output...\n"
$JQ -s "{ignore: [], names: .[0], paths: .[1], file: \"$botania_path/$lang_file\"}" \
	<($JQ "$lang_filter" $lang_file) \
	<(cd $patchouli_path; $FIND -type f -exec $JQ '{(.name): input_filename[2:-5]}' {} ';' |$JQ -s 'add') > "$output"
printf >&2 "done.\n"
