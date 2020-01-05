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

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.common.block.tile.TileConjured;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BlockConjured extends Block {

	public static final BooleanProperty SOLID = BooleanProperty.create("solid");
	public static final BooleanProperty LIGHT = BooleanProperty.create("light");
	public static final BooleanProperty BLOCK_UP = BooleanProperty.create("block_up");
	public static final BooleanProperty BLOCK_DOWN = BooleanProperty.create("block_down");
	public static final BooleanProperty BLOCK_NORTH = BooleanProperty.create("block_north");
	public static final BooleanProperty BLOCK_SOUTH = BooleanProperty.create("block_south");
	public static final BooleanProperty BLOCK_WEST = BooleanProperty.create("block_west");
	public static final BooleanProperty BLOCK_EAST = BooleanProperty.create("block_east");

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private static final Set<BlockPos> needsParticleUpdate = Sets.newHashSet();

	protected static final VoxelShape LIGHT_SHAPE = Block.makeCuboidShape(4, 4, 4, 12, 12, 12);
	
	public BlockConjured() {
		super(Block.Properties.create(Material.GLASS).variableOpacity().noDrops());
		setRegistryName(LibMisc.MOD_ID, LibBlockNames.CONJURED);
		setDefaultState(getStateContainer().getBaseState().with(LIGHT, false).with(SOLID, false));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity inWorld = worldIn.getTileEntity(pos);
		if (inWorld instanceof TileConjured)
			needsParticleUpdate.add(pos.toImmutable());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void fireParticles(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			World world = Minecraft.getInstance().world;
			Entity viewPoint = Minecraft.getInstance().getRenderViewEntity();
			if (viewPoint == null)
				viewPoint = Minecraft.getInstance().player;
			final Entity view = viewPoint;

			needsParticleUpdate.removeIf((pos) -> {
				if (view == null || world == null)
					return true;

				if (world.isBlockLoaded(pos) && view.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= 64 * 64) {
					TileEntity inWorld = world.getTileEntity(pos);
					if (inWorld instanceof TileConjured) {
						((TileConjured) inWorld).doParticles();
						return false;
					}
				}

				return true;
			});
		}
	}

	@Override
	public void tick(BlockState state, World world, BlockPos pos, Random rand) {
		world.removeBlock(pos, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST);
	}

	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(BlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public BlockState updatePostPlacement(@Nonnull BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		BooleanProperty prop;
		switch (facing) {
			default:
			case DOWN: prop = BLOCK_DOWN; break;
			case UP: prop = BLOCK_UP; break;
			case NORTH: prop = BLOCK_NORTH; break;
			case SOUTH: prop = BLOCK_SOUTH; break;
			case WEST: prop = BLOCK_WEST; break;
			case EAST: prop = BLOCK_EAST; break;
		}

		if (state.getBlock() == facingState.getBlock() && state.get(LIGHT) == facingState.get(LIGHT) && state.get(SOLID) == facingState.get(SOLID)) {
			return state.with(prop, true);
		} else {
			return state.with(prop, false);
		}
	}

	@Override
	public int getLightValue(BlockState state) {
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
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, IBlockReader world) {
		return new TileConjured();
	}


}
