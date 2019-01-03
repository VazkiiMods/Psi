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
package vazkii.psi.common.core.handler.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICADData extends INBTSerializable<NBTTagCompound> {
	int getTime();

	void setTime(int time);

	int getBattery();

	void setBattery(int battery);

	boolean isDirty();

	void markDirty(boolean isDirty);
}
