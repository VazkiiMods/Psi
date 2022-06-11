/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;

import vazkii.psi.common.block.tile.TileCADAssembler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockCADAssembler extends HorizontalDirectionalBlock implements EntityBlock {

	public BlockCADAssembler(Properties props) {
		super(props);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
		if (tile != null) {
			return tile.getCapability(ITEM_HANDLER_CAPABILITY)
					.map(ItemHandlerHelper::calcRedstoneFromInventory)
					.orElse(0);
		}

		return 0;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult rayTraceResult) {
		if (!world.isClientSide) {
			MenuProvider container = state.getMenuProvider(world, pos);
			if (container != null) {
				NetworkHooks.openGui((ServerPlayer) playerIn, container, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TileCADAssembler) {
			return (MenuProvider) te;
		}
		return null;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileCADAssembler(pos, state);
	}

	@Override
	public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock() && !isMoving) {
			TileCADAssembler te = (TileCADAssembler) world.getBlockEntity(pos);
			if (te != null) {
				for (int i = 0; i < te.getInventory().getSlots(); i++) {
					ItemStack stack = te.getInventory().getStackInSlot(i);
					if (!stack.isEmpty()) {
						Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
					}
				}
			}
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

}
