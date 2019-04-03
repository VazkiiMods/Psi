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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class CapabilitySocketable {

	public static void register() {
		CapabilityManager.INSTANCE.register(ISocketableCapability.class, CapabilityFactory.INSTANCE, SocketWheel::new);
	}

	private static class CapabilityFactory implements Capability.IStorage<ISocketableCapability> {

		private static final CapabilityFactory INSTANCE = new CapabilityFactory();

		@Override
		public NBTBase writeNBT(Capability<ISocketableCapability> capability, ISocketableCapability instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ISocketableCapability> capability, ISocketableCapability instance, EnumFacing side, NBTBase nbt) {
			if (nbt instanceof NBTTagCompound)
				instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	private static final ResourceLocation SOCKET = new ResourceLocation(LibMisc.MOD_ID, "socketable");

	@SubscribeEvent
	public static void attachSocketableCapability(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof ISocketable)
			event.addCapability(SOCKET, new SocketWrapper(event.getObject()));
	}
}
