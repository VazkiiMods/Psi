/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

import java.util.function.Supplier;

public class MessageDeductPsi {

	private final int prev;
	private final int current;
	private final int cd;
	private final boolean shatter;

	public MessageDeductPsi(int prev, int current, int cd, boolean shatter) {
		this.prev = prev;
		this.current = current;
		this.cd = cd;
		this.shatter = shatter;
	}

	public MessageDeductPsi(FriendlyByteBuf buf) {
		this.prev = buf.readVarInt();
		this.current = buf.readVarInt();
		this.cd = buf.readVarInt();
		this.shatter = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(prev);
		buf.writeVarInt(current);
		buf.writeVarInt(cd);
		buf.writeBoolean(shatter);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Player player = Psi.proxy.getClientPlayer();
			if(player != null) {
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
