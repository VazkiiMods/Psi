@echo off
:: Vazkii's JSON creator for blocks
:: Put in your /resources/assets/%modid%/models/block
:: Makes basic block JSON files as well as the acossiated item and simple blockstate
:: Can make multiple blocks at once
::
:: Usage:
:: _make (blocks name 1) (blocks name 2) (blocks name x)
::
:: Change this to your mod's ID
set modid=psi

setlocal enabledelayedexpansion

for %%x in (%*) do (
	echo Making %%x.json block
	(
		echo {
		echo     "parent": "block/cube_all",
		echo     "textures": {
		echo         "all": "%modid%:blocks/%%x"
		echo     }
		echo }
	) > %%x.json

	echo Making %%x.json item
	(
		echo {
	    echo     "parent": "%modid%:block/%%x"
		echo }
	) > ../item/%%x.json

	echo Making %%x.json blockstate
	(
		echo {
		echo   "forge_marker": 1,
		echo   "defaults": {
		echo     "model": "%modid%:%%x"
		echo   },
		echo   "variants": {
		echo     "normal": [{}],
		echo     "inventory": [{}]
		echo   }
		echo }
	) > ../../blockstates/%%x.json
)
