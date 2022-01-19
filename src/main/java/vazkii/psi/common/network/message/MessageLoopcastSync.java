/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.function.Supplier;

public class MessageLoopcastSync {

	private final int entityId;
	private final byte loopcastState;

	public MessageLoopcastSync(int entityId, boolean isLoopcasting, InteractionHand hand) {
		this.entityId = entityId;
		loopcastState = (byte) ((isLoopcasting ? 1 : 0) | (hand == null ? 0 : hand.ordinal() << 1));
	}

	public MessageLoopcastSync(FriendlyByteBuf buf) {
		entityId = buf.readVarInt();
		loopcastState = buf.readByte();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(entityId);
		buf.writeByte(loopcastState);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		boolean isLoopcasting = (loopcastState & 0b1) != 0;

		InteractionHand loopcastHand = isLoopcasting ? ((loopcastState & 0b10) != 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND) : null;

		context.get().enqueueWork(() -> {
			Player mcPlayer = Psi.proxy.getClientPlayer();
			if (mcPlayer == null) {
				return;
			}
			Level world = mcPlayer.level;

			Entity player = null;
			if (world != null) {
				player = world.getEntity(entityId);
			} else if (mcPlayer.getId() == entityId) {
				player = mcPlayer;
			}

			if (player instanceof Player) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get((Player) player);
				data.loopcasting = isLoopcasting;
				data.loopcastHand = loopcastHand;
			}
		});

		return true;
	}

}
