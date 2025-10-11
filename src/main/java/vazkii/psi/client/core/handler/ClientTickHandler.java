/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = PsiAPI.MOD_ID)
public class ClientTickHandler {

	public static int ticksInGame = 0;
	public static float partialTicks = 0.0F;
	public static float total = 0.0F;

	private static boolean lastJumpKeyState = false;

	public ClientTickHandler() {}

	private static void calcDelta() {
		total = (float) ticksInGame + partialTicks;
	}

	@SubscribeEvent
	public static void renderTick(RenderFrameEvent.Pre event) {
		partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);

	}

	@SubscribeEvent
	public static void renderTick(RenderFrameEvent.Post event) {
		calcDelta();
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		boolean pressed = mc.options.keyJump.consumeClick();

		if(mc.player != null && pressed && (!lastJumpKeyState && !mc.player.onGround())) {
			PsiArmorEvent.post(new PsiArmorEvent(mc.player, PsiArmorEvent.JUMP));
			MessageRegister.sendToServer(new MessageTriggerJumpSpell());
		}

		lastJumpKeyState = pressed;
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		HUDHandler.tick();
		Screen gui = mc.screen;

		if(gui == null && KeybindHandler.keybind.isDown()) {
			KeybindHandler.keyDown();
		}

		if(!mc.isPaused()) {
			++ticksInGame;
			partialTicks = 0.0F;
		}

		calcDelta();
	}

}
