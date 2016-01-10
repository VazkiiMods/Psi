@echo off
:: Vazkii's JSON creator for blocks
:: Put in your /resources/assets/%modid%/models/block
:: Makes basic block JSON files as well as the acossiated item and simple blockstate
:: Can make multiple blocks at once
::
:: Usage:
:: _make (block name 1) (block name 2) (block name x)
::
:: Change this to your mod's ID
set modid=psi

setlocal enabledelayedexpansion

for %%x in (%*) do (
	echo Making %%x.json block
	(
		echo {
		echo 	"parent": "block/cube_all",
		echo 	"textures": {
		echo 		"all": "%modid%:blocks/%%x"
		echo 	}
		echo }
	) > %%x.json

	echo Making %%x.json item
	(
		echo {
		echo 	"parent": "%modid%:block/%%x"
		echo 	"display": {
		echo 		"thirdperson": {
		echo 			"rotation": [ 10, -45, 170 ],
		echo 			"translation": [ 0, 1.5, -2.75 ],
		echo 			"scale": [ 0.375, 0.375, 0.375 ]
		echo 		}
		echo 	}
		echo }
	) > ../item/%%x.json

	echo Making %%x.json blockstate
	(
		echo {
		echo 	"forge_marker": 1,
		echo 	"defaults": {
		echo 		"model": "%modid%:%%x"
		echo 	},
		echo 	"variants": {
		echo 		"normal": [{}],
		echo 		"inventory": [{}]
		echo 	}
		echo }
	) > ../../blockstates/%%x.json
)
