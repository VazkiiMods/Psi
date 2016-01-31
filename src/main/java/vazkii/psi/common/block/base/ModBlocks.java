/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 23:13:36 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.psi.common.block.BlockCADAssembler;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.BlockPsiDecorative;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibResources;

public class ModBlocks {

	public static BlockMod cadAssembler; 
	public static BlockMod programmer; 
	public static BlockMod psiDecorative; 

	public static void init() {
		cadAssembler = new BlockCADAssembler();
		programmer = new BlockProgrammer();
		psiDecorative = new BlockPsiDecorative();

		initTileEntities();
	}

	private static void initTileEntities() {
		registerTile(TileCADAssembler.class, LibBlockNames.CAD_ASSEMBLER);
		registerTile(TileProgrammer.class, LibBlockNames.PROGRAMMER);
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, LibResources.PREFIX_MOD + key);
	}
	
}
