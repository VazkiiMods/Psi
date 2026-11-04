/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public final class FabricPsiGameplayEvents {
	private FabricPsiGameplayEvents() {}

	public static void register() {
		ServerPlayerEvents.JOIN.register(player -> {
			PlayerDataHandler.onPlayerLogin(player);
			LoopcastTrackingHandler.syncDataFor(player, player);
		});
		// Fabric's attachment API copies copyOnDeath data in the default AFTER_RESPAWN phase.
		ResourceLocation afterDataCopy = PsiAPI.location("after_data_copy");
		ServerPlayerEvents.AFTER_RESPAWN.addPhaseOrdering(Event.DEFAULT_PHASE, afterDataCopy);
		ServerPlayerEvents.AFTER_RESPAWN.register(afterDataCopy, (oldPlayer, newPlayer, alive) -> {
			PlayerDataHandler.syncData(newPlayer);
			LoopcastTrackingHandler.syncDataFor(newPlayer, newPlayer);
		});
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			PlayerDataHandler.onChangeDimension(player);
			LoopcastTrackingHandler.syncDataFor(player, player);
		});
		EntityTrackingEvents.START_TRACKING.register((tracked, receiver) -> {
			if(tracked instanceof Player player) {
				LoopcastTrackingHandler.syncDataFor(player, receiver);
			}
		});
	}
}
