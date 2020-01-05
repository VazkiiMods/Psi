/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [12/01/2016, 16:11:55 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.api.cad.ICADData;

public class MessageCADDataSync implements IMessage {

	public CompoundNBT cmp;

	public MessageCADDataSync() { }

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeForSynchronization();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if (!cad.isEmpty()) {
				cad.getCapability(ICADData.CAPABILITY).ifPresent(d -> d.deserializeNBT(cmp));
			}
		});

		return true;
	}

}
