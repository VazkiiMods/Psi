/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

import java.util.function.Supplier;

public class MessageDataSync {

	private final CompoundNBT cmp;

	public MessageDataSync(PlayerData data) {
		cmp = new CompoundNBT();
		data.writeToNBT(cmp);
	}

	public MessageDataSync(PacketBuffer buf) {
		cmp = buf.readNbt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeNbt(cmp);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
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
