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

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.common.block.base.BlockModContainer;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.lib.LibBlockNames;

public class BlockConjured extends BlockModContainer {

	public static final PropertyBool SOLID = PropertyBool.create("solid");
	public static final PropertyBool LIGHT = PropertyBool.create("light");
	public static final PropertyBool BLOCK_UP = PropertyBool.create("block_up");
	public static final PropertyBool BLOCK_DOWN = PropertyBool.create("block_down");
	public static final PropertyBool BLOCK_NORTH = PropertyBool.create("block_north");
	public static final PropertyBool BLOCK_SOUTH = PropertyBool.create("block_south");
	public static final PropertyBool BLOCK_WEST = PropertyBool.create("block_west");
	public static final PropertyBool BLOCK_EAST = PropertyBool.create("block_east");
	
	public BlockConjured() {
		super(LibBlockNames.CONJURED, Material.glass);
		setDefaultState(makeDefaultState());
		setLightOpacity(0);
	}
	
	public IBlockState makeDefaultState() {
		return getStateFromMeta(0);
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, getAllProperties());
	}
	
	@Override
	public IProperty[] getIgnoredProperties() {
		return getAllProperties(); 
	}
	
	public IProperty[] getAllProperties() {
		return new IProperty[] { SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST };
	}
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
	
    @Override
    public boolean isFullCube() {
        return false;
    }
    
	@Override
	public boolean isFullBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isVisuallyOpaque() {
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
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(LIGHT) ? 15 : 0;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return state.getValue(SOLID) ? super.getCollisionBoundingBox(worldIn, pos, state) : null;
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		boolean solid = state.getValue(SOLID);
		float f = solid ? 0F : 0.25F;
		
		double minX = f;
		double minY = f;
		double minZ = f;
		double maxX = 1F - f; 
		double maxY = 1F - f; 
		double maxZ = 1F - f;
		
		return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + maxY, pos.getZ() + maxZ);
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileConjured();
	}
	
	
}
