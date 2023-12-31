/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageCADDataSync {

	private final CompoundTag cmp;

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeForSynchronization();
	}

	public MessageCADDataSync(FriendlyByteBuf buf) {
		cmp = buf.readNbt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(cmp);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if(!cad.isEmpty()) {
				cad.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(d -> d.deserializeNBT(cmp));
			}
		});

		return true;
	}

}
