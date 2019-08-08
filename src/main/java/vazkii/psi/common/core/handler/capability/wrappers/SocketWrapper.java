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
package vazkii.psi.common.core.handler.capability.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SocketWrapper implements ISocketableCapability, ICapabilityProvider {

	private final ItemStack stack;
	private final ISocketable item;

	public SocketWrapper(ItemStack stack) {
		this.stack = stack;
		this.item = (ISocketable) stack.getItem();
	}


	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
		return capability == CAPABILITY;
	}

	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return capability == CAPABILITY ? CAPABILITY.cast(this) : null;
	}

	@Override
	public boolean isSocketSlotAvailable(int slot) {
		return item.isSocketSlotAvailable(stack, slot);
	}

	@Override
	public boolean showSlotInRadialMenu(int slot) {
		return item.showSlotInRadialMenu(stack, slot);
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		return item.getBulletInSocket(stack, slot);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		item.setBulletInSocket(stack, slot, bullet);
	}

	@Override
	public int getSelectedSlot() {
		return item.getSelectedSlot(stack);
	}

	@Override
	public void setSelectedSlot(int slot) {
		item.setSelectedSlot(stack, slot);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack bullet) {
		return item.isItemValid(stack, slot, bullet);
	}

	@Override
	public boolean canLoopcast(ItemStack stack) {
		return item.canLoopcast(stack);
	}
}
