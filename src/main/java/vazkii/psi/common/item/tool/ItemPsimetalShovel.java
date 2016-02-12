/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 20:15:11 (GMT)]
 */
package vazkii.psi.common.item.tool;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalShovel extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass, Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand});

	public ItemPsimetalShovel() {
		super(LibItemNames.PSIMETAL_SHOVEL, 1F, EFFECTIVE_ON);
	}

	// ItemSpade copypasta:

	@Override
	public boolean canHarvestBlock(Block blockIn) {
		return blockIn == Blocks.snow_layer ? true : blockIn == Blocks.snow;
	}

}
