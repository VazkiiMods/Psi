/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 03, 2019, 15:16 AM (EST)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import vazkii.psi.api.internal.IPlayerData;

public interface ISocketableCapability extends INBTSerializable<NBTTagCompound> {
	@CapabilityInject(ISocketableCapability.class)
	Capability<ISocketableCapability> CAPABILITY = null;

	static boolean isSocketable(ItemStack stack) {
		return stack.hasCapability(CAPABILITY, null);
	}

	static ISocketableCapability socketable(ItemStack stack) {
		return stack.getCapability(CAPABILITY, null);
	}

	boolean isSocketSlotAvailable(int slot);

	default boolean showSlotInRadialMenu(int slot) {
		return isSocketSlotAvailable(slot);
	}

	ItemStack getBulletInSocket(int slot);

	void setBulletInSocket(int slot, ItemStack bullet);

	int getSelectedSlot();

	void setSelectedSlot(int slot);

	boolean isItemValid(int slot, ItemStack bullet);

	boolean canLoopcast(ItemStack stack);

	default boolean showPsiBar(IPlayerData data) {
		return true;
	}
}
