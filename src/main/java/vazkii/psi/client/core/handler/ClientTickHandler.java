/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [11/01/2016, 17:51:36 (GMT)]
 */
package vazkii.psi.client.core.handler;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class ClientTickHandler {

	public static volatile Queue<Runnable> scheduledActions = new ArrayDeque<>();

	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	private void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		if(event.phase == Phase.START)
			partialTicks = event.renderTickTime;
	}

	@SubscribeEvent
	public void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.world == null)
				PlayerDataHandler.cleanup();
			else if(mc.player != null) {
				PersistencyHandler.init();

				while(!scheduledActions.isEmpty())
					scheduledActions.poll().run();
			}

			HUDHandler.tick();

			GuiScreen gui = mc.currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) {
				if(gui == null && KeybindHandler.keybind.isKeyDown())
					KeybindHandler.keyDown();

				ticksInGame++;
				partialTicks = 0;
			}

			calcDelta();
		}
	}

}
