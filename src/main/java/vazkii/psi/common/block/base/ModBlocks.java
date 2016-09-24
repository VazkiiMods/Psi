/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 23:13:36 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.block.BlockMod;
import vazkii.psi.common.block.BlockCADAssembler;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.BlockPsiDecorative;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibResources;

public class ModBlocks {

	public static BlockMod cadAssembler;
	public static BlockMod programmer;
	public static BlockMod psiDecorative;
	public static BlockMod conjured;

	public static void init() {
		cadAssembler = new BlockCADAssembler();
		programmer = new BlockProgrammer();
		psiDecorative = new BlockPsiDecorative();
		conjured = new BlockConjured();

		initTileEntities();

		// Psi oredict mappings
		OreDictionary.registerOre("blockPsiDust", new ItemStack(psiDecorative, 1, 0));
		OreDictionary.registerOre("blockPsiMetal", new ItemStack(psiDecorative, 1, 1));
		OreDictionary.registerOre("blockPsiGem", new ItemStack(psiDecorative, 1, 2));
	}

	private static void initTileEntities() {
		registerTile(TileCADAssembler.class, LibBlockNames.CAD_ASSEMBLER);
		registerTile(TileProgrammer.class, LibBlockNames.PROGRAMMER);
		registerTile(TileConjured.class, LibBlockNames.CONJURED);
	}

	private static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, LibResources.PREFIX_MOD + key);
	}

}
