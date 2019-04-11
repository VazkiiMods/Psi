/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [28/02/2016, 20:28:03 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;
import vazkii.psi.api.internal.Vector3;

public interface ICADData extends INBTSerializable<NBTTagCompound> {
	@CapabilityInject(ICADData.class)
	Capability<ICADData> CAPABILITY = null;

	static boolean hasData(ItemStack stack) {
		return stack.hasCapability(CAPABILITY, null);
	}

	static ICADData data(ItemStack stack) {
		return stack.getCapability(CAPABILITY, null);
	}

	int getTime();

	void setTime(int time);

	int getBattery();

	void setBattery(int battery);

	Vector3 getSavedVector(int memorySlot);

	void setSavedVector(int memorySlot, Vector3 value);

	boolean isDirty();

	void markDirty(boolean isDirty);

	NBTTagCompound serializeForSynchronization();
}
