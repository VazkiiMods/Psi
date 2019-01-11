/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [12/01/2016, 16:45:17 (GMT)]
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
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class MessageDeductPsi extends NetworkMessage<MessageDeductPsi> {

	public int prev;
	public int current;
	public int cd;
	public boolean shatter;

	public MessageDeductPsi() { }

	public MessageDeductPsi(int prev, int current, int cd, boolean shatter) {
		this.prev = prev;
		this.current = current;
		this.cd = cd;
		this.shatter = shatter;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			EntityPlayer player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerData data = PlayerDataHandler.get(player);
				data.lastAvailablePsi = data.availablePsi;
				data.availablePsi = current;
				data.regenCooldown = cd;
				data.deductTick = true;
				data.addDeduction(prev, prev - current, shatter);
			}
		});

		return null;
	}

}
