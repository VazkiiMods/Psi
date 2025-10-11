/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageLoopcastSync;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public class LoopcastTrackingHandler {
	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if(event.getTarget() instanceof Player) {
			syncDataFor((Player) event.getTarget(), (ServerPlayer) event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		syncDataFor(event.getEntity(), (ServerPlayer) event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		syncDataFor(event.getEntity(), (ServerPlayer) event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		syncDataFor(event.getEntity(), (ServerPlayer) event.getEntity());
	}

	public static void syncDataFor(Player player, ServerPlayer receiver) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);

		MessageRegister.sendToPlayer(receiver, new MessageLoopcastSync(player.getId(), data.loopcasting, data.loopcastHand));
	}

	public static void syncForTrackersAndSelf(ServerPlayer playerEntity) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerEntity);
		MessageLoopcastSync messageLoopcastSync = new MessageLoopcastSync(playerEntity.getId(), data.loopcasting, data.loopcastHand);
		MessageRegister.sendToPlayersTrackingEntity(playerEntity, messageLoopcastSync);
		MessageRegister.sendToPlayer(playerEntity, messageLoopcastSync);
	}
}
