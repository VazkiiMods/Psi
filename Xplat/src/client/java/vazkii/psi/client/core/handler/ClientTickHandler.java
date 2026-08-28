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

import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;

public class ClientTickHandler {

	public static int ticksInGame = 0;
	public static float partialTicks = 0.0F;
	public static float total = 0.0F;

	private static boolean lastJumpKeyState = false;

	public ClientTickHandler() {}

	private static void calcDelta() {
		total = (float) ticksInGame + partialTicks;
	}

	public static void renderTick(float partialTick) {
		partialTicks = partialTick;
		calcDelta();
	}

	public static void clientTickPre() {
		Minecraft mc = Minecraft.getInstance();
		BookSoundHandler.tick();
		boolean pressed = mc.options.keyJump.consumeClick();

		if(mc.player != null && pressed && (!lastJumpKeyState && !mc.player.onGround())) {
			PsiArmorEvent.post(new PsiArmorEvent(mc.player, PsiArmorEvent.JUMP));
			PsiNetwork.sendToServer(new MessageTriggerJumpSpell());
		}

		lastJumpKeyState = pressed;
	}

	public static void clientTickPost() {
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
