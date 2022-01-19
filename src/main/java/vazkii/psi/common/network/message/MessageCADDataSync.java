/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageCADDataSync {

	private final CompoundNBT cmp;

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeForSynchronization();
	}

	public MessageCADDataSync(PacketBuffer buf) {
		cmp = buf.readNbt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeNbt(cmp);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if (!cad.isEmpty()) {
				cad.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(d -> d.deserializeNBT(cmp));
			}
		});

		return true;
	}

}
