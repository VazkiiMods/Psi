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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.capability.CapabilityCAD;
import vazkii.psi.common.core.handler.capability.ICADData;

public class MessageCADDataSync extends NetworkMessage<MessageCADDataSync> {

	public NBTTagCompound cmp;

	public MessageCADDataSync() { }

	public MessageCADDataSync(ICADData data) {
		cmp = data.serializeNBT();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
			if (!cad.isEmpty() && cad.hasCapability(CapabilityCAD.CAPABILITY, null)) {
				ICADData data = cad.getCapability(CapabilityCAD.CAPABILITY, null);

				if (data != null)
					data.deserializeNBT(cmp);
			}
		});

		return null;
	}

}
