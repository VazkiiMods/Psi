/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.common.block.tile.TileCADAssembler;

public class BlockCADAssembler extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<BlockCADAssembler> CODEC = simpleCodec(BlockCADAssembler::new);

	public BlockCADAssembler(Properties props) {
		super(props);
	}

	@Override
	public MapCodec<BlockCADAssembler> codec() {
		return CODEC;
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
		IItemHandler handler = worldIn.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
		if(handler != null) {
			return ItemHandlerHelper.calcRedstoneFromInventory(handler);
		}
		return 0;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player playerIn, BlockHitResult rayTraceResult) {
		if(world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			MenuProvider container = state.getMenuProvider(world, pos);
			if(container != null) {
				playerIn.openMenu(container, pos);
			}
		}
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if(te instanceof TileCADAssembler) {
			return (MenuProvider) te;
		}
		return null;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TileCADAssembler(pos, state);
	}

	@Override
	public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock() && !isMoving) {
			TileCADAssembler te = (TileCADAssembler) world.getBlockEntity(pos);
			if(te != null) {
				for(int i = 0; i < te.getInventory().getSlots(); i++) {
					ItemStack stack = te.getInventory().getStackInSlot(i);
					if(!stack.isEmpty()) {
						Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
					}
				}
			}
		}

		super.onRemove(state, world, pos, newState, isMoving);
	}

}
