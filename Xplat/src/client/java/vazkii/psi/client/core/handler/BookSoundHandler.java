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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;

import org.lwjgl.glfw.GLFW;

import vazkii.patchouli.api.PatchouliAPI;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.lib.LibResources;

import java.util.Objects;

// https://github.com/Vazkii/Botania/blob/faeaf7285b0c9ef0918ad1cb2cbff88ed3ea1d65/src/main/java/vazkii/botania/client/core/handler/KonamiHandler.java
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

	public static void tick() {
		if(bookTime > 0) {
			bookTime--;
		}

		if(!isBookOpen()) {
			nextLetter = 0;
		}
	}

	public static void handleInput(long windowPointer, int key, int action, int modifiers) {
		Minecraft mc = Minecraft.getInstance();
		if(windowPointer == mc.getWindow().getWindow() && modifiers == 0 && action == GLFW.GLFW_PRESS && isBookOpen()) {
			if(bookTime == 0 && key == SECRET_CODE[nextLetter]) {
				nextLetter++;
				if(nextLetter >= SECRET_CODE.length) {
					mc.getSoundManager().play(SimpleSoundInstance.forUI(PsiSoundHandler.book.get(), 1.0F));
					nextLetter = 0;
					bookTime = 320;
				}
			} else {
				nextLetter = 0;
			}
		}
	}
}
