/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 20:16:28 (GMT)]
 */
package vazkii.psi.common.item.tool;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalAxe extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder});

	public ItemPsimetalAxe() {
		super(LibItemNames.PSIMETAL_AXE, 8F, -3.1F, EFFECTIVE_ON);
	}

	// ItemAxe copypasta:

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		return state.getMaterial() != Material.wood && state.getMaterial() != Material.plants && state.getMaterial() != Material.vine ? super.getStrVsBlock(stack, state) : efficiencyOnProperMaterial;
	}

}
