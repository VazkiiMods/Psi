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

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;

public record MessageLoopcastSync(int entityId, byte loopcastState) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_loopcast_sync");
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

	public void handle(Player clientPlayer) {
		boolean isLoopcasting = (loopcastState & 0b1) != 0;
		InteractionHand loopcastHand = isLoopcasting ? ((loopcastState & 0b10) != 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND) : null;

		Level world = clientPlayer.level();
		Entity entity = clientPlayer.getId() == entityId ? clientPlayer : world.getEntity(entityId);
		if(entity instanceof Player player) {
			PlayerData data = PsiPlayerData.get(player);
			if(data.loopcasting != isLoopcasting || data.loopcastHand != loopcastHand) {
				data.lastTickLoopcastStack = null;
				data.loopcastTime = 1;
				data.loopcastAmount = 0;
				data.loopcastFadeTime = isLoopcasting ? 0 : 5;
			}
			data.loopcasting = isLoopcasting;
			data.loopcastHand = loopcastHand;
		}
	}
}
