/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.common.item.tool.IPsimetalTool;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinNeoForgeItemStack {

	@Inject(
		method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"),
		cancellable = true
	)
	private void keepBrokenPsimetalGear(int damage, ServerLevel level, LivingEntity entity,
			Consumer<Item> onBroken, CallbackInfo callback) {
		if(((ItemStack) (Object) this).getItem() instanceof IPsimetalTool) {
			callback.cancel();
		}
	}
}
