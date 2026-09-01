/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.AdditiveMotionHandler;
import vazkii.psi.common.core.handler.DelayedSpellHandler;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public final class NeoForgePsiGameplayEvents {

	private NeoForgePsiGameplayEvents() {}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		AdditiveMotionHandler.onLevelTick(event.getLevel());
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event) {
		DelayedSpellHandler.tick();
	}

	@SubscribeEvent
	public static void onEntityDamage(LivingDamageEvent.Pre event) {
		PlayerDataHandler.onEntityDamage(event.getEntity(), event.getSource(), event.getNewDamage());
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.getEntity() instanceof ServerPlayer player) {
			PlayerDataHandler.onPlayerLogin(player);
			LoopcastTrackingHandler.syncDataFor(player, player);
		}
	}

	@SubscribeEvent
	public static void onAdvancementProgress(AdvancementEvent.AdvancementProgressEvent event) {
		if(event.getProgressType() != AdvancementEvent.AdvancementProgressEvent.ProgressType.GRANT || !event.getAdvancementProgress().isDone()) {
			return;
		}

		if(event.getEntity() instanceof ServerPlayer player) {
			PlayerDataHandler.onAdvancementCompleted(player, event.getAdvancement().id());
		}
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		Player player = event.getEntity();
		PlayerDataHandler.onChangeDimension(player);
		LoopcastTrackingHandler.syncDataFor(player, (ServerPlayer) player);
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		LoopcastTrackingHandler.syncDataFor(event.getEntity(), (ServerPlayer) event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if(event.getTarget() instanceof Player target) {
			LoopcastTrackingHandler.syncDataFor(target, (ServerPlayer) event.getEntity());
		}
	}

}
