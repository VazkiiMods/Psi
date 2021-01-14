/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileConjured;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public class BlockConjured extends Block implements IWaterLoggable {

	public static final BooleanProperty SOLID = BooleanProperty.create("solid");
	public static final BooleanProperty LIGHT = BooleanProperty.create("light");
	public static final BooleanProperty BLOCK_UP = BooleanProperty.create("block_up");
	public static final BooleanProperty BLOCK_DOWN = BooleanProperty.create("block_down");
	public static final BooleanProperty BLOCK_NORTH = BooleanProperty.create("block_north");
	public static final BooleanProperty BLOCK_SOUTH = BooleanProperty.create("block_south");
	public static final BooleanProperty BLOCK_WEST = BooleanProperty.create("block_west");
	public static final BooleanProperty BLOCK_EAST = BooleanProperty.create("block_east");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape LIGHT_SHAPE = Block.makeCuboidShape(4, 4, 4, 12, 12, 12);

	public BlockConjured(Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(LIGHT, false).with(SOLID, false).with(WATERLOGGED, false).with(BLOCK_DOWN, false).with(BLOCK_UP, false).with(BLOCK_EAST, false).with(BLOCK_WEST, false).with(BLOCK_NORTH, false).with(BLOCK_SOUTH, false));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity inWorld = worldIn.getTileEntity(pos);
		if (inWorld instanceof TileConjured) {
			((TileConjured) inWorld).doParticles();
		}
	}

	@Nullable
	@Override
	public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
		TileEntity inWorld = world.getTileEntity(pos);
		if (inWorld instanceof TileConjured) {
			int color = Psi.proxy.getColorForColorizer(((TileConjured) inWorld).colorizer);
			return new float[] { PsiRenderHelper.r(color) / 255F, PsiRenderHelper.g(color) / 255F, PsiRenderHelper.b(color) / 255F };
		}
		return null;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.removeBlock(pos, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST, WATERLOGGED);
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public BlockState updatePostPlacement(@Nonnull BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		BooleanProperty prop;
		switch (facing) {
		default:
		case DOWN:
			prop = BLOCK_DOWN;
			break;
		case UP:
			prop = BLOCK_UP;
			break;
		case NORTH:
			prop = BLOCK_NORTH;
			break;
		case SOUTH:
			prop = BLOCK_SOUTH;
			break;
		case WEST:
			prop = BLOCK_WEST;
			break;
		case EAST:
			prop = BLOCK_EAST;
			break;
		}
		if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (state.getBlock() == facingState.getBlock() && state.get(LIGHT) == facingState.get(LIGHT) && state.get(SOLID) == facingState.get(SOLID)) {
			return state.with(prop, true);
		} else {
			return state.with(prop, false);
		}
	}

	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(LIGHT) ? 15 : 0;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return state.get(SOLID) ? VoxelShapes.fullCube() : VoxelShapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return state.get(SOLID) ? VoxelShapes.fullCube() : LIGHT_SHAPE;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, IBlockReader world) {
		return new TileConjured();
	}

}
