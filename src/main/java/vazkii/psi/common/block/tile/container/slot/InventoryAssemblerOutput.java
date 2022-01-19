/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import vazkii.psi.common.block.tile.TileCADAssembler;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 *         Created at 4:44 PM on 1/2/19.
 */
public class InventoryAssemblerOutput implements IInventory {

	private final PlayerEntity player;
	private final TileCADAssembler assembler;

	public InventoryAssemblerOutput(PlayerEntity player, TileCADAssembler assembler) {
		this.player = player;
		this.assembler = assembler;
	}

	private ItemStack getStack() {
		return assembler.getCachedCAD(player);
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return getStack().isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getItem(int index) {
		return getStack();
	}

	@Nonnull
	@Override
	public ItemStack removeItem(int index, int count) {
		return getStack();
	}

	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return getStack();
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack stack) {
		// NO-OP
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void setChanged() {
		// NO-OP
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return true;
	}

	@Override
	public void startOpen(@Nonnull PlayerEntity player) {
		// NO-OP
	}

	@Override
	public void stopOpen(@Nonnull PlayerEntity player) {
		// NO-OP
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public void clearContent() {
		// NO-OP
	}
}
