/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mixin(PlayerAdvancements.class)
public abstract class MixinPlayerAdvancements {
	@Shadow
	private ServerPlayer player;

	@Inject(
		method = "award",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"
		)
	)
	private void onAdvancementCompleted(AdvancementHolder advancement, String criterion, CallbackInfoReturnable<Boolean> callback) {
		PlayerDataHandler.onAdvancementCompleted(player, advancement.id());
	}
}
