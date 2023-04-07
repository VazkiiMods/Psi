/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

import java.util.function.Supplier;

public class MessageDataSync {

	private final CompoundTag cmp;

	public MessageDataSync(PlayerData data) {
		cmp = new CompoundTag();
		data.writeToNBT(cmp);
	}

	public MessageDataSync(FriendlyByteBuf buf) {
		cmp = buf.readNbt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(cmp);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Player player = Psi.proxy.getClientPlayer();
			if (player != null) {
				PlayerData data = PlayerDataHandler.get(player);
				data.lastAvailablePsi = data.availablePsi;
				data.readFromNBT(cmp);
			}
		});

		return true;
	}

}
