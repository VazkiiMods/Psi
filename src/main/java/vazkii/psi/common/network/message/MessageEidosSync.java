/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Feb 02, 2019, 10:20 AM (EST)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageEidosSync implements IMessage {

	public int reversionTime;

	public MessageEidosSync() { }

	public MessageEidosSync(int reversionTime) {
		this.reversionTime = reversionTime;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			PlayerEntity player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				data.eidosReversionTime = reversionTime;
				data.isReverting = true;
			}
		});

		return true;
	}

}
