/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpellModified;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockCADAssembler extends HorizontalBlock {

	public BlockCADAssembler(Properties props) {
		super(props);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getDefaultState().with(HORIZONTAL_FACING, ctx.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null) {
			return tile.getCapability(ITEM_HANDLER_CAPABILITY)
					.map(ItemHandlerHelper::calcRedstoneFromInventory)
					.orElse(0);
		}

		return 0;
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult rayTraceResult) {
		if (!world.isRemote) {
			INamedContainerProvider container = state.getContainer(world, pos);
			if (container != null) {
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, container, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	@Override
	public INamedContainerProvider getContainer(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileCADAssembler) {
			return (INamedContainerProvider) te;
		}
		return null;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileCADAssembler();
	}
}
