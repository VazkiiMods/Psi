/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Inject(method = "jumpFromGround", at = @At("TAIL"))
	private void triggerPsiJump(CallbackInfo callback) {
		if((Object) this instanceof Player player) {
			PlayerDataHandler.onEntityJump(player);
		}
	}
}
