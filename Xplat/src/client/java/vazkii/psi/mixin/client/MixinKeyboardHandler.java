/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin.client;

import net.minecraft.client.KeyboardHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.client.core.handler.BookSoundHandler;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
	@Inject(method = "keyPress", at = @At("HEAD"))
	private void psi$handleBookCode(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo callbackInfo) {
		BookSoundHandler.handleInput(windowPointer, key, action, modifiers);
	}
}
