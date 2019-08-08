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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class MessageDataSync extends NetworkMessage<MessageDataSync> {

	public CompoundNBT cmp;

	public MessageDataSync() { }

	public MessageDataSync(PlayerData data) {
		cmp = new CompoundNBT();
		data.writeToNBT(cmp);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			PlayerEntity player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerData data = PlayerDataHandler.get(player);
				data.lastAvailablePsi = data.availablePsi;
				data.readFromNBT(cmp);
				Psi.proxy.savePersistency();
			}
		});

		return null;
	}

}
