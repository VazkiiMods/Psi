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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
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


    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return PsiAPI.CAD_DATA_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> this));
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
		return (vec == null ? Vector3.zero : vec).copy();
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
	public CompoundNBT serializeForSynchronization() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("Time", time);
        compound.putInt("Battery", battery);

        return compound;
    }

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = serializeForSynchronization();

		ListNBT memory = new ListNBT();
		for (Vector3 vector : vectors) {
            if (vector == null)
                memory.add(new ListNBT());
            else {
                ListNBT vec = new ListNBT();
                vec.add(DoubleNBT.of(vector.x));
                vec.add(DoubleNBT.of(vector.y));
                vec.add(DoubleNBT.of(vector.z));
                memory.add(vec);
            }
        }
        compound.put("Memory", memory);

        return compound;
    }

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("Time", Constants.NBT.TAG_ANY_NUMERIC))
            time = nbt.getInt("Time");
        if (nbt.contains("Battery", Constants.NBT.TAG_ANY_NUMERIC))
            battery = nbt.getInt("Battery");

        if (nbt.contains("Memory", Constants.NBT.TAG_LIST)) {
            ListNBT memory = nbt.getList("Memory", Constants.NBT.TAG_LIST);
            List<Vector3> newVectors = Lists.newArrayList();
            for (int i = 0; i < memory.size(); i++) {
                ListNBT vec = (ListNBT) memory.get(i);
                if (vec.getTagType() == Constants.NBT.TAG_DOUBLE && vec.size() >= 3)
                    newVectors.add(new Vector3(vec.getDouble(0), vec.getDouble(1), vec.getDouble(2)));
                else
                    newVectors.add(null);
            }
            vectors = newVectors;
        }
	}
}
