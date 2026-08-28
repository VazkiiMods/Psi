/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import vazkii.psi.api.internal.Vector3;

public interface ICADData {

	CompoundTag serializeNBT(HolderLookup.Provider registries);

	void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag);

	/**
	 * Gets the total amount of the CAD has been on the player's inventory
	 */
	int getTime();

	/**
	 * Sets the total amount of time the CAD has been on the player's inventory
	 */
	void setTime(int time);

	/**
	 * Gets the total capacity of the CAD's battery
	 */
	int getBattery();

	/**
	 * Sets the battery capacity currently in the CAD
	 */
	void setBattery(int battery);

	/**
	 * Gets the vector in the respective memory slot
	 */
	Vector3 getSavedVector(int memorySlot);

	/**
	 * Sets the vector in the respective memory slot
	 */
	void setSavedVector(int memorySlot, Vector3 value);

	/**
	 * Serializes the CAD data to be synchronized
	 */
	CompoundTag serializeForSynchronization();
}
