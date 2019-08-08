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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.common.core.handler.PersistencyHandler;
import vazkii.psi.common.lib.LibMisc;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class ClientTickHandler {

	/**
	 * @deprecated Use {@link vazkii.arl.util.ClientTicker#partialTicks}
	 */
	@Deprecated
	public static float partialTicks;

	@SuppressWarnings("deprecation")
	private static void updatePartialTicks() {
		partialTicks = ClientTicker.partialTicks;
	}

	@SubscribeEvent
	public static void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			updatePartialTicks();

			Minecraft mc = Minecraft.getMinecraft();
			if(mc.player != null)
				PersistencyHandler.init();

			HUDHandler.tick();

			Screen gui = mc.currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) {
				if(gui == null && KeybindHandler.keybind.isKeyDown())
					KeybindHandler.keyDown();
			}
		}
	}

}
