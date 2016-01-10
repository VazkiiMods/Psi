/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://botaniamod.net/license.php
 * 
 * File Created @ [09/01/2016, 23:13:36 (GMT)]
 */
package vazkii.psi.common.block.base;

import com.ibm.icu.util.Calendar;

import vazkii.psi.common.block.BlockCADAssembler;

public class ModBlocks {

	public static BlockMod cadAssembler; 
	
	public static void init() {
		cadAssembler = new BlockCADAssembler();

		initTileEntities();
	}

	private static void initTileEntities() {
		
	}
	
}
