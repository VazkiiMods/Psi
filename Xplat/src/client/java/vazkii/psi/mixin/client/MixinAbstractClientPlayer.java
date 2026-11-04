/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.psi.client.core.handler.PlayerDataRenderHandler;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {
	@Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
	private void psi$modifyAnchoredFov(CallbackInfoReturnable<Float> callbackInfo) {
		float fov = PlayerDataRenderHandler.modifyFov((AbstractClientPlayer) (Object) this, callbackInfo.getReturnValue());
		callbackInfo.setReturnValue(fov);
	}
}
