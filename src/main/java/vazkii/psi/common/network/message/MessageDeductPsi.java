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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class MessageDeductPsi implements IMessage {

	public int prev;
	public int current;
	public int cd;
	public boolean shatter;

	public MessageDeductPsi() {}

	public MessageDeductPsi(int prev, int current, int cd, boolean shatter) {
		this.prev = prev;
		this.current = current;
		this.cd = cd;
		this.shatter = shatter;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			PlayerEntity player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerData data = PlayerDataHandler.get(player);
				data.lastAvailablePsi = data.availablePsi;
				data.availablePsi = current;
				data.regenCooldown = cd;
				data.deductTick = true;
				data.addDeduction(prev, prev - current, shatter);
			}
		});

		return true;
	}

}
