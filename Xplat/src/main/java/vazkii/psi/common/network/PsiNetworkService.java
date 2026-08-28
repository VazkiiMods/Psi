/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public interface PsiNetworkService {

	void initialize();

	<T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler);

	<T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler);

	void sendToServer(CustomPacketPayload payload);

	void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);

	void sendToPlayersTracking(Entity entity, CustomPacketPayload payload);

	void sendToPlayersTrackingAndSelf(Entity entity, CustomPacketPayload payload);

	void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload);

}
