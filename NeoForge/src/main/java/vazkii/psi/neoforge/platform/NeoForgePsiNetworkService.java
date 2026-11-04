/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.network.PsiNetworkService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class NeoForgePsiNetworkService implements PsiNetworkService {

	private static final String VERSION = "4";

	private final List<Registration<?>> registrations = new ArrayList<>();

	@Override
	public void initialize() {
		NeoForgePsiPlatform.modBus().addListener(this::registerPayloads);
	}

	@Override
	public <T extends CustomPacketPayload> void registerClientbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		registrations.add(new Registration<>(type, codec, handler, Direction.CLIENTBOUND));
	}

	@Override
	public <T extends CustomPacketPayload> void registerServerbound(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler) {
		registrations.add(new Registration<>(type, codec, handler, Direction.SERVERBOUND));
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		PacketDistributor.sendToServer(payload);
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	@Override
	public void sendToPlayersTracking(Entity entity, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
	}

	@Override
	public void sendToPlayersTrackingAndSelf(Entity entity, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload);
	}

	@Override
	public void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayersInDimension(level, payload);
	}

	private void registerPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(PsiAPI.MOD_ID).versioned(VERSION);
		registrations.forEach(registration -> registration.register(registrar));
	}

	private enum Direction {
		CLIENTBOUND,
		SERVERBOUND
	}

	private record Registration<T extends CustomPacketPayload>(CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, Player> handler,
			Direction direction) {

		private void register(PayloadRegistrar registrar) {
			if(direction == Direction.CLIENTBOUND) {
				registrar.playToClient(type, codec,
						(payload, context) -> context.enqueueWork(() -> handler.accept(payload, context.player())));
			} else {
				registrar.playToServer(type, codec,
						(payload, context) -> context.enqueueWork(() -> handler.accept(payload, context.player())));
			}
		}

	}

}
