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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalShovel extends ItemPsimetalTool {

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND});

	public ItemPsimetalShovel() {
		super(LibItemNames.PSIMETAL_SHOVEL, 1.5F, -3.0F, EFFECTIVE_ON);
		setHarvestLevel("shovel", 3);
	}

	// ItemSpade copypasta:

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		Block blockIn = state.getBlock();
		return blockIn == Blocks.SNOW_LAYER ? true : blockIn == Blocks.SNOW;
	}
	
	@Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	return Items.IRON_SHOVEL.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
	
}
