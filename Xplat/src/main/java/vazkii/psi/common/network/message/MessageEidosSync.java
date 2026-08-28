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
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.core.handler.PsiPlayerData;

public record MessageEidosSync(int reversionTime) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_eidos_sync");
	public static final CustomPacketPayload.Type<MessageEidosSync> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageEidosSync> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, MessageEidosSync::reversionTime,
			MessageEidosSync::new);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		PlayerData data = PsiPlayerData.get(player);
		data.eidosReversionTime = reversionTime;
		data.isReverting = true;
	}
}
