/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [28/02/2016, 20:28:40 (GMT)]
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CADData implements ICapabilityProvider, ICADData {

	private int time;
	private int battery;

	private boolean dirty;

	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityCAD.CAPABILITY;
	}

	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityCAD.CAPABILITY ? CapabilityCAD.CAPABILITY.cast(this) : null;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void setTime(int time) {
		if (this.time != time)
		this.time = time;
	}

	@Override
	public int getBattery() {
		return battery;
	}

	@Override
	public void setBattery(int battery) {
		this.battery = battery;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void markDirty(boolean isDirty) {
		dirty = isDirty;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Time", time);
		compound.setInteger("Battery", battery);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		time = nbt.getInteger("Time");
		battery = nbt.getInteger("Battery");
	}
}
