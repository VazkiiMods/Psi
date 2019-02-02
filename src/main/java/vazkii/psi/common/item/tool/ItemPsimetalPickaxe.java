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

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemPsimetalPickaxe extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB);

	public ItemPsimetalPickaxe() {
		super(LibItemNames.PSIMETAL_PICKAXE, 1.0F, -2.8F, EFFECTIVE_ON);
		setHarvestLevel("pickaxe", 3);
	}

	// ItemPickaxe copypasta:

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		Block blockIn = state.getBlock();
		return blockIn == Blocks.OBSIDIAN ? toolMaterial.getHarvestLevel() == 3 : blockIn != Blocks.DIAMOND_BLOCK && blockIn != Blocks.DIAMOND_ORE ? blockIn != Blocks.EMERALD_ORE && blockIn != Blocks.EMERALD_BLOCK ? blockIn != Blocks.GOLD_BLOCK && blockIn != Blocks.GOLD_ORE ? blockIn != Blocks.IRON_BLOCK && blockIn != Blocks.IRON_ORE ? blockIn != Blocks.LAPIS_BLOCK && blockIn != Blocks.LAPIS_ORE ? blockIn != Blocks.REDSTONE_ORE && blockIn != Blocks.LIT_REDSTONE_ORE ? state.getMaterial() == Material.ROCK || (state.getMaterial() == Material.IRON || state.getMaterial() == Material.ANVIL) : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 1 : toolMaterial.getHarvestLevel() >= 1 : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 2 : toolMaterial.getHarvestLevel() >= 2;
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
		return state.getMaterial() != Material.IRON && state.getMaterial() != Material.ANVIL && state.getMaterial() != Material.ROCK ? super.getDestroySpeed(stack, state) : efficiency;
	}

}
