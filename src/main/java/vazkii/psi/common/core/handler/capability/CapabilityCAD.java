/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [28/02/2016, 20:27:15 (GMT)]
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import vazkii.psi.api.cad.ICADData;

public class CapabilityCAD {
	public static void register() {
		CapabilityManager.INSTANCE.register(ICADData.class, CapabilityFactory.INSTANCE, CADData::new);
	}

	private static class CapabilityFactory implements Capability.IStorage<ICADData> {

		private static final CapabilityFactory INSTANCE = new CapabilityFactory();

		@Override
		public NBTBase writeNBT(Capability<ICADData> capability, ICADData instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ICADData> capability, ICADData instance, EnumFacing side, NBTBase nbt) {
			if (nbt instanceof NBTTagCompound)
				instance.deserializeNBT((NBTTagCompound) nbt);
		}

	}


}
