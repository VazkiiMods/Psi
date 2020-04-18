/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.common.Psi;

public class MessageCADDataSync implements IMessage {

	public CompoundNBT cmp;

	public MessageCADDataSync() {}

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeForSynchronization();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if (!cad.isEmpty()) {
				cad.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(d -> d.deserializeNBT(cmp));
			}
		});

		return true;
	}

}
