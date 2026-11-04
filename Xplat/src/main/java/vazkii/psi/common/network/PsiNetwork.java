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

import vazkii.psi.common.platform.PsiServices;

import java.util.function.BiConsumer;

public final class PsiNetwork {

	private static final PsiNetworkService SERVICE = PsiServices.load(PsiNetworkService.class);

	private PsiNetwork() {}

	public static void initialize() {
		SERVICE.initialize();
	}

	public static <T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		SERVICE.registerClientbound(type, codec, handler);
	}

	public static <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		SERVICE.registerServerbound(type, codec, handler);
	}

	public static void sendToServer(CustomPacketPayload payload) {
		SERVICE.sendToServer(payload);
	}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		SERVICE.sendToPlayer(player, payload);
	}

	public static void sendToPlayersTracking(Entity entity, CustomPacketPayload payload) {
		SERVICE.sendToPlayersTracking(entity, payload);
	}

	public static void sendToPlayersTrackingAndSelf(Entity entity, CustomPacketPayload payload) {
		SERVICE.sendToPlayersTrackingAndSelf(entity, payload);
	}

	public static void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload) {
		SERVICE.sendToPlayersInDimension(level, payload);
	}

}
