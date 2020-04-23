/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class MessageDataSync implements IMessage {

	public CompoundNBT cmp;

	public MessageDataSync() {}

	public MessageDataSync(PlayerData data) {
		cmp = new CompoundNBT();
		data.writeToNBT(cmp);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			PlayerEntity player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerData data = PlayerDataHandler.get(player);
				data.lastAvailablePsi = data.availablePsi;
				data.readFromNBT(cmp);
			}
		});

		return true;
	}

}
