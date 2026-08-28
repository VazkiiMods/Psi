/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityDamage {
	@Inject(
		method = "actuallyHurt",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;getAbsorptionAmount()F",
			ordinal = 0
		)
	)
	private void beforeAbsorption(DamageSource source, float damage, CallbackInfo callback) {
		PlayerDataHandler.onEntityDamage((LivingEntity) (Object) this, source, damage);
	}
}
