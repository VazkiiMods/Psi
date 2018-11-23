/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/02/2016, 18:23:26 (GMT)]
 */
package vazkii.psi.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.arl.block.BlockModContainer;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.lib.LibBlockNames;

public class BlockConjured extends BlockModContainer implements IPsiBlock {

	public static final PropertyBool SOLID = PropertyBool.create("solid");
	public static final PropertyBool LIGHT = PropertyBool.create("light");
	public static final PropertyBool BLOCK_UP = PropertyBool.create("block_up");
	public static final PropertyBool BLOCK_DOWN = PropertyBool.create("block_down");
	public static final PropertyBool BLOCK_NORTH = PropertyBool.create("block_north");
	public static final PropertyBool BLOCK_SOUTH = PropertyBool.create("block_south");
	public static final PropertyBool BLOCK_WEST = PropertyBool.create("block_west");
	public static final PropertyBool BLOCK_EAST = PropertyBool.create("block_east");

    protected static final AxisAlignedBB LIGHT_AABB = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
	
	public BlockConjured() {
		super(LibBlockNames.CONJURED, Material.GLASS);
		setDefaultState(makeDefaultState());
		setLightOpacity(0);
	}

	public IBlockState makeDefaultState() {
		return getStateFromMeta(0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, getAllProperties());
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return getAllProperties();
	}

	public IProperty[] getAllProperties() {
		return new IProperty[] { SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST };
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		return state.withProperty(SOLID, (meta & 1) > 0).withProperty(LIGHT, (meta & 2) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(SOLID) ? 1 : 0) + (state.getValue(LIGHT) ? 2 : 0);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState origState = state;
		state = state.withProperty(BLOCK_UP, worldIn.getBlockState(pos.up()).equals(origState));
		state = state.withProperty(BLOCK_DOWN, worldIn.getBlockState(pos.down()).equals(origState));
		state = state.withProperty(BLOCK_NORTH, worldIn.getBlockState(pos.north()).equals(origState));
		state = state.withProperty(BLOCK_SOUTH, worldIn.getBlockState(pos.south()).equals(origState));
		state = state.withProperty(BLOCK_WEST, worldIn.getBlockState(pos.west()).equals(origState));
		state = state.withProperty(BLOCK_EAST, worldIn.getBlockState(pos.east()).equals(origState));

		return state;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(LIGHT) ? 15 : 0;
	}

	@Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity, boolean blarg) {
		if(state.getValue(SOLID))
			addCollisionBoxToList(pos, aabb, list, new AxisAlignedBB(0, 0, 0, 1, 1, 1));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean solid = state.getValue(SOLID);
		return solid ? FULL_BLOCK_AABB : LIGHT_AABB;
	}

	@Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileConjured();
	}


}
