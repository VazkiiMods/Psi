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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;

import java.util.Objects;

// https://github.com/Vazkii/Botania/blob/faeaf7285b0c9ef0918ad1cb2cbff88ed3ea1d65/src/main/java/vazkii/botania/client/core/handler/KonamiHandler.java
@EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT)
public class BookSoundHandler {
	private static final int[] SECRET_CODE = {
			GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_U,
			GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_T,
			GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_0,
			GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_4
	};
	private static int nextLetter = 0;
	private static int bookTime = 0;

	private static boolean isBookOpen() {
		return Objects.equals(PatchouliAPI.get().getOpenBookGui(), LibResources.PATCHOULI_BOOK);
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Pre evt) {
		if(bookTime > 0) {
			bookTime--;
		}

		if(!isBookOpen()) {
			nextLetter = 0;
		}
	}

	@SubscribeEvent
	public static void handleInput(InputEvent.Key evt) {
		Minecraft mc = Minecraft.getInstance();
		if(evt.getModifiers() == 0 && evt.getAction() == GLFW.GLFW_PRESS && isBookOpen()) {
			if(bookTime == 0 && evt.getKey() == SECRET_CODE[nextLetter]) {
				nextLetter++;
				if(nextLetter >= SECRET_CODE.length) {
					mc.getSoundManager().play(SimpleSoundInstance.forUI(PsiSoundHandler.book, 1.0F));
					nextLetter = 0;
					bookTime = 320;
				}
			} else {
				nextLetter = 0;
			}
		}
	}
}
