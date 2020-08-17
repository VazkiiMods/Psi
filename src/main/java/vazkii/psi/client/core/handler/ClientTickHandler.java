/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.psi.common.lib.LibMisc;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class ClientTickHandler {

	public static int ticksInGame = 0;
	public static float partialTicks = 0.0F;
	public static float delta = 0.0F;
	public static float total = 0.0F;

	public ClientTickHandler() {}

	@OnlyIn(Dist.CLIENT)
	private static void calcDelta() {
		float oldTotal = total;
		total = (float) ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void renderTick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;
		} else {
			calcDelta();
		}
	}

	@SubscribeEvent
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {

			Minecraft mc = Minecraft.getInstance();

			HUDHandler.tick();

			Screen gui = mc.currentScreen;
			if (gui == null || !gui.isPauseScreen()) {
				++ticksInGame;
				partialTicks = 0.0F;
				if (gui == null && KeybindHandler.keybind.isKeyDown()) {
					KeybindHandler.keyDown();
				}
			}

			calcDelta();
		}
	}

}
