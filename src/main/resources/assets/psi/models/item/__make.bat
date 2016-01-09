@echo off
setlocal enabledelayedexpansion


for %%x in (%*) do (
	echo Making %%x.json
	(
		echo {
	    echo     "parent": "psi:item/_standard_item",
	    echo     "textures": { 
	   	echo         "layer0": "psi:items/%%x"
	    echo     }
		echo }
	) > %%x.json

)
