/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.function.Supplier;

public class MessageLoopcastSync {

	private final int entityId;
	private final byte loopcastState;

	public MessageLoopcastSync(int entityId, boolean isLoopcasting, Hand hand) {
		this.entityId = entityId;
		loopcastState = (byte) ((isLoopcasting ? 1 : 0) | (hand == null ? 0 : hand.ordinal() << 1));
	}

	public MessageLoopcastSync(PacketBuffer buf) {
		entityId = buf.readVarInt();
		loopcastState = buf.readByte();
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(entityId);
		buf.writeByte(loopcastState);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		boolean isLoopcasting = (loopcastState & 0b1) != 0;

		Hand loopcastHand = isLoopcasting ? ((loopcastState & 0b10) != 0 ? Hand.OFF_HAND : Hand.MAIN_HAND) : null;

		context.get().enqueueWork(() -> {
			PlayerEntity mcPlayer = Psi.proxy.getClientPlayer();
			if (mcPlayer == null) {
				return;
			}
			World world = mcPlayer.world;

			Entity player = null;
			if (world != null) {
				player = world.getEntityByID(entityId);
			} else if (mcPlayer.getEntityId() == entityId) {
				player = mcPlayer;
			}

			if (player instanceof PlayerEntity) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get((PlayerEntity) player);
				data.loopcasting = isLoopcasting;
				data.loopcastHand = loopcastHand;
			}
		});

		return true;
	}

}
