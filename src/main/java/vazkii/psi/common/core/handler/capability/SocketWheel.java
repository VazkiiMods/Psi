/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import vazkii.psi.api.cad.ISocketable;

public class SocketWheel implements ISocketable, INBTSerializable<CompoundNBT> {

	private final int size;

	private final ItemStackHandler handler;

	private int selectedSlot = 0;

	public SocketWheel() {
		this(ISocketable.MAX_SLOTS);
	}

	public SocketWheel(int size) {
		this.size = size;
		this.handler = new ItemStackHandler(size);
	}

	@Override
	public boolean isSocketSlotAvailable(int slot) {
		return slot < size;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		return handler.getStackInSlot(slot);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		handler.setStackInSlot(slot, bullet);
	}

	@Override
	public int getSelectedSlot() {
		return selectedSlot;
	}

	@Override
	public void setSelectedSlot(int slot) {
		selectedSlot = slot;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack bullet) {
		return handler.insertItem(slot, bullet, true).isEmpty();
	}

	@Override
	public boolean canLoopcast() {
		return false;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = handler.serializeNBT();
		compound.remove("Size");
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		nbt.putInt("Size", size);
		handler.deserializeNBT(nbt);
	}
}
