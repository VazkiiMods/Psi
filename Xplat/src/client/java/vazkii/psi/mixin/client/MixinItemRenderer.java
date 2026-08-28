/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin.client;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.psi.api.cad.ICAD;
import vazkii.psi.client.model.ClientModelHandler;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
	@Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
	private void psi$selectCadModel(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, int seed,
			CallbackInfoReturnable<BakedModel> callbackInfo) {
		if(stack.getItem() instanceof ICAD) {
			callbackInfo.setReturnValue(ClientModelHandler.resolveCadModel(stack));
		}
	}
}
