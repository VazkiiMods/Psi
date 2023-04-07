/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageLoopcastSync;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class LoopcastTrackingHandler {
	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof Player) {
			syncDataFor((Player) event.getTarget(), (ServerPlayer) event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		syncDataFor(event.getPlayer(), (ServerPlayer) event.getPlayer());
	}

	@SubscribeEvent
	public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		syncDataFor(event.getPlayer(), (ServerPlayer) event.getPlayer());
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		syncDataFor(event.getPlayer(), (ServerPlayer) event.getPlayer());
	}

	public static void syncDataFor(Player player, ServerPlayer receiver) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);

		MessageRegister.sendToPlayer(new MessageLoopcastSync(player.getId(), data.loopcasting, data.loopcastHand), receiver);
	}

	public static void syncForTrackers(ServerPlayer player) {

		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageLoopcastSync(player.getId(), data.loopcasting, data.loopcastHand));

	}

	public static void syncForTrackersAndSelf(ServerPlayer playerEntity) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerEntity);
		MessageLoopcastSync messageLoopcastSync = new MessageLoopcastSync(playerEntity.getId(), data.loopcasting, data.loopcastHand);
		MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> playerEntity), messageLoopcastSync);
		MessageRegister.sendToPlayer(messageLoopcastSync, playerEntity);
	}
}
