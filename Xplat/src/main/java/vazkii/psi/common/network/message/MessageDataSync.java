/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundTag;
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

public record MessageDataSync(CompoundTag cmp) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_data_sync");
	public static final CustomPacketPayload.Type<MessageDataSync> TYPE = new CustomPacketPayload.Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, MessageDataSync> CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG, MessageDataSync::cmp,
			MessageDataSync::new);

	public MessageDataSync(PlayerData data) {
		this(new CompoundTag());
		data.writeToNBT(cmp);
	}

	@Override
	public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		PlayerData data = PsiPlayerData.get(player);
		data.lastAvailablePsi = data.availablePsi;
		data.readFromNBT(cmp);
	}

}
