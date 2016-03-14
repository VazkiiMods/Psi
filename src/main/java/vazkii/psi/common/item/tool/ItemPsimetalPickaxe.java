/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 20:09:14 (GMT)]
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

public class ItemPsimetalPickaxe extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab});

	public ItemPsimetalPickaxe() {
		super(LibItemNames.PSIMETAL_PICKAXE, 1.0F, -2.8F, EFFECTIVE_ON);
	}

	// ItemPickaxe copypasta:

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		Block blockIn = state.getBlock();
		return blockIn == Blocks.obsidian ? toolMaterial.getHarvestLevel() == 3 : blockIn != Blocks.diamond_block && blockIn != Blocks.diamond_ore ? blockIn != Blocks.emerald_ore && blockIn != Blocks.emerald_block ? blockIn != Blocks.gold_block && blockIn != Blocks.gold_ore ? blockIn != Blocks.iron_block && blockIn != Blocks.iron_ore ? blockIn != Blocks.lapis_block && blockIn != Blocks.lapis_ore ? blockIn != Blocks.redstone_ore && blockIn != Blocks.lit_redstone_ore ? state.getMaterial() == Material.rock ? true : state.getMaterial() == Material.iron ? true : state.getMaterial() == Material.anvil : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 1 : toolMaterial.getHarvestLevel() >= 1 : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 2;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		return state.getMaterial() != Material.iron && state.getMaterial() != Material.anvil && state.getMaterial() != Material.rock ? super.getStrVsBlock(stack, state) : efficiencyOnProperMaterial;
	}

}
