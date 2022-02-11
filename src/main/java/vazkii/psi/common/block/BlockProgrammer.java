/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.UUID;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockProgrammer extends HorizontalDirectionalBlock implements EntityBlock {

	public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
	private static final VoxelShape SHAPE_NORTH;
	private static final VoxelShape SHAPE_SOUTH;
	private static final VoxelShape SHAPE_WEST;
	private static final VoxelShape SHAPE_EAST;
	static {
		VoxelShape top = Block.box(0, 8, 0, 16, 16, 16);

		VoxelShape northMiddle = Block.box(2, 0, 14, 14, 8, 16);
		VoxelShape southMiddle = Block.box(2, 0, 0, 14, 8, 2);
		VoxelShape zBottom = Block.box(2, 0, 0, 14, 1, 16);
		SHAPE_NORTH = Shapes.join(top, Shapes.join(zBottom, northMiddle, BooleanOp.OR), BooleanOp.OR);
		SHAPE_SOUTH = Shapes.join(top, Shapes.join(zBottom, southMiddle, BooleanOp.OR), BooleanOp.OR);

		VoxelShape westMiddle = Block.box(14, 0, 2, 16, 8, 14);
		VoxelShape eastMiddle = Block.box(0, 0, 2, 2, 8, 14);
		VoxelShape xBottom = Block.box(0, 0, 2, 16, 1, 14);
		SHAPE_WEST = Shapes.join(top, Shapes.join(xBottom, westMiddle, BooleanOp.OR), BooleanOp.OR);
		SHAPE_EAST = Shapes.join(top, Shapes.join(xBottom, eastMiddle, BooleanOp.OR), BooleanOp.OR);
	}

	public BlockProgrammer(Properties props) {
		super(props);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ENABLED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		switch (state.getValue(FACING)) {
		default:
		case NORTH:
			return SHAPE_NORTH;
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		ItemStack heldItem = player.getItemInHand(hand);
		TileProgrammer programmer = (TileProgrammer) worldIn.getBlockEntity(pos);
		if (programmer == null) {
			return InteractionResult.PASS;
		}

		InteractionResult result = setSpell(worldIn, pos, player, heldItem);
		if (result == InteractionResult.SUCCESS) {
			return InteractionResult.SUCCESS;
		}

		boolean enabled = programmer.isEnabled();
		if (!enabled || programmer.playerLock.isEmpty()) {
			programmer.playerLock = player.getName().getString();
		}

		if (player instanceof ServerPlayer) {
			VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (ServerPlayer) player);
		}
		if (worldIn.isClientSide) {
			Psi.proxy.openProgrammerGUI(programmer);
		}
		return InteractionResult.SUCCESS;
	}

	public InteractionResult setSpell(Level worldIn, BlockPos pos, Player playerIn, ItemStack heldItem) {
		TileProgrammer programmer = (TileProgrammer) worldIn.getBlockEntity(pos);
		if (programmer == null) {
			return InteractionResult.FAIL;
		}

		boolean enabled = programmer.isEnabled();

		LazyOptional<ISpellAcceptor> settable = heldItem.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY);
		if (enabled && !heldItem.isEmpty() && settable.isPresent() && programmer.spell != null && (playerIn.isShiftKeyDown() || !settable.orElse(null).requiresSneakForSpellSet())) {
			if (programmer.canCompile()) {
				if (!worldIn.isClientSide) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundSource.BLOCKS, 0.5F, 1F);
				}

				programmer.spell.uuid = UUID.randomUUID();
				settable.ifPresent(c -> c.setSpell(playerIn, programmer.spell));
				if (playerIn instanceof ServerPlayer) {
					VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (ServerPlayer) playerIn);
				}
				return InteractionResult.SUCCESS;
			} else {
				if (!worldIn.isClientSide) {
					worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.compileError, SoundSource.BLOCKS, 0.5F, 1F);
				}
				return InteractionResult.FAIL;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileProgrammer(pos, state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		BlockEntity tile = worldIn.getBlockEntity(pos);
		if (tile instanceof TileProgrammer) {
			TileProgrammer programmer = (TileProgrammer) tile;

			if (programmer.canCompile()) {
				return 2;
			} else if (programmer.isEnabled()) {
				return 1;
			} else {
				return 0;
			}
		}

		return 0;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		return super.getMenuProvider(state, worldIn, pos);
	}

}
