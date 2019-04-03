/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 03, 2019, 15:19 AM (EST)]
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableCapability;

public class SocketWheel implements ISocketableCapability {

	private final int size;

	private final ItemStackHandler handler;

	private int selectedSlot = 0;

	public SocketWheel() {
		this(ISocketable.MAX_SLOTS);
	}

	public SocketWheel(int size) {
		this.size = size;
		this.handler =  new ItemStackHandler(size);
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
	public boolean canLoopcast(ItemStack stack) {
		return false;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = handler.serializeNBT();
		compound.removeTag("Size");
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		nbt.setInteger("Size", size);
		handler.deserializeNBT(nbt);
	}
}
