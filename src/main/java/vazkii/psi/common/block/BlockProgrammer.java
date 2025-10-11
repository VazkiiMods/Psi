/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;

import java.util.UUID;

public class BlockProgrammer extends HorizontalDirectionalBlock implements EntityBlock {
	public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
	public static final MapCodec<BlockProgrammer> CODEC = simpleCodec(BlockProgrammer::new);

	public BlockProgrammer(Properties props) {
		super(props);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ENABLED, false));
	}

	@Override
	public @NotNull MapCodec<BlockProgrammer> codec() {
		return CODEC;
	}

	@Override
	protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if(setSpell(pLevel, pPos, pPlayer, pStack) == InteractionResult.SUCCESS) {
			return ItemInteractionResult.SUCCESS;
		}

		return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
		TileProgrammer programmer = (TileProgrammer) pLevel.getBlockEntity(pPos);
		if(programmer == null) {
			return InteractionResult.PASS;
		}
		boolean enabled = programmer.isEnabled();
		if(!enabled || programmer.playerLock.isEmpty()) {
			programmer.playerLock = pPlayer.getName().getString();
		}

		if(pPlayer instanceof ServerPlayer) {
			VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (ServerPlayer) pPlayer);
		}
		if(pLevel.isClientSide) {
			Psi.proxy.openProgrammerGUI(programmer);
		}
		return InteractionResult.SUCCESS;
	}

	public InteractionResult setSpell(Level pLevel, BlockPos pPos, Player pPlayer, ItemStack pStack) {
		TileProgrammer programmer = (TileProgrammer) pLevel.getBlockEntity(pPos);
		if(programmer == null) {
			return InteractionResult.FAIL;
		}
		boolean enabled = programmer.isEnabled();

		ISpellAcceptor settable = pStack.getCapability(PsiAPI.SPELL_ACCEPTOR_CAPABILITY);
		if(enabled && !pStack.isEmpty() && settable != null && programmer.spell != null && (pPlayer.isShiftKeyDown() || !settable.requiresSneakForSpellSet())) {
			if(programmer.canCompile()) {
				if(!pLevel.isClientSide) {
					pLevel.playSound(null, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundSource.BLOCKS, 0.5F, 1F);
				}

				programmer.spell.uuid = UUID.randomUUID();
				settable.setSpell(pPlayer, programmer.spell);
				if(pPlayer instanceof ServerPlayer) {
					VanillaPacketDispatcher.dispatchTEToPlayer(programmer, (ServerPlayer) pPlayer);
				}
				return InteractionResult.SUCCESS;
			} else {
				if(!pLevel.isClientSide) {
					pLevel.playSound(null, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, PsiSoundHandler.compileError, SoundSource.BLOCKS, 0.5F, 1F);
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
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TileProgrammer(pos, state);
	}

	@Override
	public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(@NotNull BlockState blockState, Level worldIn, @NotNull BlockPos pos) {
		BlockEntity tile = worldIn.getBlockEntity(pos);
		if(tile instanceof TileProgrammer programmer) {

			if(programmer.canCompile()) {
				return 2;
			} else if(programmer.isEnabled()) {
				return 1;
			} else {
				return 0;
			}
		}

		return 0;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos) {
		return super.getMenuProvider(state, worldIn, pos);
	}

}
