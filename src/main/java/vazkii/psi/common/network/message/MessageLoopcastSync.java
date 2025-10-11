/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public record MessageLoopcastSync(int entityId, byte loopcastState) implements CustomPacketPayload {

	public static final ResourceLocation ID = Psi.location("message_loopcast_sync");
	public static final CustomPacketPayload.Type<MessageLoopcastSync> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageLoopcastSync> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, MessageLoopcastSync::entityId,
			ByteBufCodecs.BYTE, MessageLoopcastSync::loopcastState,
			MessageLoopcastSync::new);

	public MessageLoopcastSync(int entityId, boolean isLoopcasting, InteractionHand hand) {
		this(entityId, (byte) ((isLoopcasting ? 1 : 0) | (hand == null ? 0 : hand.ordinal() << 1)));
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		boolean isLoopcasting = (loopcastState & 0b1) != 0;

		InteractionHand loopcastHand = isLoopcasting ? ((loopcastState & 0b10) != 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND) : null;

		ctx.enqueueWork(() -> {
			Player mcPlayer = Psi.proxy.getClientPlayer();
			if(mcPlayer == null) {
				return;
			}

			Level world = mcPlayer.level();

			Entity player = world.getEntity(entityId);
			if(mcPlayer.getId() == entityId) {
				player = mcPlayer;
			}

			if(player instanceof Player) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get((Player) player);
				data.loopcasting = isLoopcasting;
				data.loopcastHand = loopcastHand;
			}
		});

	}
}
