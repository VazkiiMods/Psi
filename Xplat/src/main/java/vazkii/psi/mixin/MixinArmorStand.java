/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mixin(ArmorStand.class)
public abstract class MixinArmorStand {
	@Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
	private void keepCadFromEquippingArmorStand(Player player, Vec3 location, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> callback) {
		if(PlayerDataHandler.blocksArmorStandInteraction(
				player, (ArmorStand) (Object) this, hand)) {
			callback.setReturnValue(InteractionResult.PASS);
		}
	}
}
