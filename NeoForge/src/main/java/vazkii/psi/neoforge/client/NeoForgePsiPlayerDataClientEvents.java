/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.handler.PlayerDataRenderHandler;

@EventBusSubscriber(modid = PsiAPI.MOD_ID, value = Dist.CLIENT)
public final class NeoForgePsiPlayerDataClientEvents {
	private NeoForgePsiPlayerDataClientEvents() {}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderLevelStageEvent event) {
		if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			return;
		}
		float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		PlayerDataRenderHandler.renderAll(partialTicks, event.getPoseStack());
	}
}
