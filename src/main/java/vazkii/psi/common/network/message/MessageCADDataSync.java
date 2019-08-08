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
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.api.cad.ICADData;

public class MessageCADDataSync extends NetworkMessage<MessageCADDataSync> {

	public CompoundNBT cmp;

	public MessageCADDataSync() { }

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeForSynchronization();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if (!cad.isEmpty() && cad.hasCapability(ICADData.CAPABILITY, null)) {
				ICADData data = cad.getCapability(ICADData.CAPABILITY, null);

				if (data != null)
					data.deserializeNBT(cmp);
			}
		});

		return null;
	}

}
