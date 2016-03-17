/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [14/01/2016, 18:46:37 (GMT)]
 */
package vazkii.psi.common.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFacing extends BlockModContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockFacing(String name, Material materialIn, String... variants) {
		super(name, materialIn, variants);
		setDefaultState(makeDefaultState());
	}

	public IBlockState makeDefaultState() {
		return blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		setDefaultFacing(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState thisState) {
		if(!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos.north());
			IBlockState state1 = worldIn.getBlockState(pos.south());
			IBlockState state2 = worldIn.getBlockState(pos.west());
			IBlockState state3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = thisState.getValue(FACING);

			if(enumfacing == EnumFacing.NORTH && state.isFullBlock() && !state1.isFullBlock())
				enumfacing = EnumFacing.SOUTH;
			else if(enumfacing == EnumFacing.SOUTH && state1.isFullBlock() && !state.isFullBlock())
				enumfacing = EnumFacing.NORTH;
			else if(enumfacing == EnumFacing.WEST && state2.isFullBlock() && !state3.isFullBlock())
				enumfacing = EnumFacing.EAST;
			else if(enumfacing == EnumFacing.EAST && state3.isFullBlock() && !state2.isFullBlock())
				enumfacing = EnumFacing.WEST;

			worldIn.setBlockState(pos, thisState.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if(enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;

		return getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

}
