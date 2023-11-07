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
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class ClientTickHandler {

	public static int ticksInGame = 0;
	public static float partialTicks = 0.0F;
	public static float delta = 0.0F;
	public static float total = 0.0F;

	private static boolean lastJumpKeyState = false;

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
		if(event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;
		} else {
			calcDelta();
		}
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {

		Minecraft mc = Minecraft.getInstance();

		if(event.phase == TickEvent.Phase.START) {

			boolean pressed = mc.options.keyJump.consumeClick();
			if(mc.player != null && pressed && (!lastJumpKeyState && !mc.player.isOnGround())) {
				PsiArmorEvent.post(new PsiArmorEvent(mc.player, PsiArmorEvent.JUMP));
				MessageRegister.HANDLER.sendToServer(new MessageTriggerJumpSpell());
			}
			lastJumpKeyState = pressed;
		}
		if(event.phase == TickEvent.Phase.END) {

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

}
