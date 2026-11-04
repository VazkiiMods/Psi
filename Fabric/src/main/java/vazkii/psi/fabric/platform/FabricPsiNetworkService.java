/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.common.network.PsiNetworkService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class FabricPsiNetworkService implements PsiNetworkService {

	private static FabricPsiNetworkService instance;

	private final List<ClientboundRegistration<?>> clientbound = new ArrayList<>();
	private Consumer<CustomPacketPayload> clientSender = payload -> {
		throw new IllegalStateException("Fabric client networking has not been initialized");
	};

	public FabricPsiNetworkService() {
		if(instance != null) {
			throw new IllegalStateException("Fabric network service was created more than once");
		}
		instance = this;
	}

	public static FabricPsiNetworkService instance() {
		if(instance == null) {
			throw new IllegalStateException("Fabric network service has not been initialized");
		}
		return instance;
	}

	@Override
	public void initialize() {}

	@Override
	public <T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		PayloadTypeRegistry.playS2C().register(type, codec);
		clientbound.add(new ClientboundRegistration<>(type, handler));
	}

	@Override
	public <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		PayloadTypeRegistry.playC2S().register(type, codec);
		ServerPlayNetworking.registerGlobalReceiver(type,
				(payload, context) -> handler.accept(payload, context.player()));
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		clientSender.accept(payload);
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	@Override
	public void sendToPlayersTracking(Entity entity, CustomPacketPayload payload) {
		PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, payload));
	}

	@Override
	public void sendToPlayersTrackingAndSelf(Entity entity, CustomPacketPayload payload) {
		sendToPlayersTracking(entity, payload);
		if(entity instanceof ServerPlayer player) {
			ServerPlayNetworking.send(player, payload);
		}
	}

	@Override
	public void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload) {
		PlayerLookup.world(level).forEach(player -> ServerPlayNetworking.send(player, payload));
	}

	public List<ClientboundRegistration<?>> clientboundRegistrations() {
		return List.copyOf(clientbound);
	}

	public void installClientSender(Consumer<CustomPacketPayload> sender) {
		clientSender = sender;
	}

	public record ClientboundRegistration<T extends CustomPacketPayload>(
			CustomPacketPayload.Type<T> type, BiConsumer<T, Player> handler) {
	}

}
