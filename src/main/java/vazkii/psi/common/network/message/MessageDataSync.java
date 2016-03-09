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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.network.Message;

public class MessageDataSync extends Message {

	public NBTTagCompound cmp;

	public MessageDataSync() { }

	public MessageDataSync(PlayerData data) {
		cmp = new NBTTagCompound();
		data.writeToNBT(cmp);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTickHandler.scheduledActions.add(() -> {
			PlayerData data = PlayerDataHandler.get(Psi.proxy.getClientPlayer());
			data.lastAvailablePsi = data.availablePsi;
			data.readFromNBT(cmp);
			Psi.proxy.savePersistency();
		});

		return null;
	}

}
