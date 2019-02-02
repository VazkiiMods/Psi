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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageEidosSync extends NetworkMessage<MessageEidosSync> {

	public int reversionTime;

	public MessageEidosSync() { }

	public MessageEidosSync(int reversionTime) {
		this.reversionTime = reversionTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			EntityPlayer player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				data.eidosReversionTime = reversionTime;
				data.isReverting = true;
			}
		});

		return null;
	}

}
