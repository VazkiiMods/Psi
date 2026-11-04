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
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.client.core.handler.ClientTickHandler;

@EventBusSubscriber(modid = PsiAPI.MOD_ID, value = Dist.CLIENT)
public final class NeoForgePsiClientTickEvents {
	private NeoForgePsiClientTickEvents() {}

	@SubscribeEvent
	public static void onRenderFrame(RenderFrameEvent.Pre event) {
		ClientTickHandler.renderTick(event.getPartialTick().getGameTimeDeltaPartialTick(false));
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		ClientTickHandler.clientTickPre();
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		ClientTickHandler.clientTickPost();
	}
}
