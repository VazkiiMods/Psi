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

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.internal.Vector3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CADData implements ICapabilityProvider, ICADData {

	private int time;
	private int battery;
	private List<Vector3> vectors = Lists.newArrayList();

	private boolean dirty;

	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CAPABILITY;
	}

	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CAPABILITY ? CAPABILITY.cast(this) : null;
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
	public Vector3 getSavedVector(int memorySlot) {
		if (vectors.size() <= memorySlot)
			return Vector3.zero.copy();

		Vector3 vec = vectors.get(memorySlot);
		return vec == null ? null : vec.copy();
	}

	@Override
	public void setSavedVector(int memorySlot, Vector3 value) {
		while (vectors.size() <= memorySlot)
			vectors.add(null);

		vectors.set(memorySlot, value);
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
	public NBTTagCompound serializeForSynchronization() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Time", time);
		compound.setInteger("Battery", battery);

		return compound;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = serializeForSynchronization();

		NBTTagList memory = new NBTTagList();
		for (Vector3 vector : vectors) {
			if (vector == null)
				memory.appendTag(new NBTTagList());
			else {
				NBTTagList vec = new NBTTagList();
				vec.appendTag(new NBTTagDouble(vector.x));
				vec.appendTag(new NBTTagDouble(vector.y));
				vec.appendTag(new NBTTagDouble(vector.z));
				memory.appendTag(vec);
			}
		}
		compound.setTag("Memory", memory);

		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("Time", Constants.NBT.TAG_ANY_NUMERIC))
			time = nbt.getInteger("Time");
		if (nbt.hasKey("Battery", Constants.NBT.TAG_ANY_NUMERIC))
			battery = nbt.getInteger("Battery");

		if (nbt.hasKey("Memory", Constants.NBT.TAG_LIST)) {
			NBTTagList memory = nbt.getTagList("Memory", Constants.NBT.TAG_LIST);
			List<Vector3> newVectors = Lists.newArrayList();
			for (int i = 0; i < memory.tagCount(); i++) {
				NBTTagList vec = (NBTTagList) memory.get(i);
				if (vec.getTagType() == Constants.NBT.TAG_DOUBLE && vec.tagCount() >= 3)
					newVectors.add(new Vector3(vec.getDoubleAt(0), vec.getDoubleAt(1), vec.getDoubleAt(2)));
				else
					newVectors.add(null);
			}
			vectors = newVectors;
		}
	}
}
