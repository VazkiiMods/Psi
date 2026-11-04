/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.psi.common.item.tool.IPsimetalTool;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	@ModifyVariable(method = "setDamageValue", at = @At("HEAD"), argsOnly = true)
	private int keepBrokenPsimetalGear(int damage) {
		ItemStack stack = (ItemStack) (Object) this;
		return stack.getItem() instanceof IPsimetalTool && damage > stack.getMaxDamage()
				? stack.getDamageValue()
				: damage;
	}

	@Inject(
		method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"),
		cancellable = true,
		require = 0
	)
	private void keepBrokenPsimetalGear(int damage, ServerLevel level, ServerPlayer player,
			Consumer<Item> onBroken, CallbackInfo callback) {
		cancelPsimetalBreak(callback);
	}

	private void cancelPsimetalBreak(CallbackInfo callback) {
		ItemStack stack = (ItemStack) (Object) this;
		if(stack.getItem() instanceof IPsimetalTool) {
			callback.cancel();
		}
	}

	@Inject(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", at = @At("HEAD"), cancellable = true)
	private void filterBrokenToolModifiers(EquipmentSlot slot,
			BiConsumer<Holder<Attribute>, AttributeModifier> consumer, CallbackInfo callback) {
		filterBrokenToolModifiers(slot, null, consumer, callback);
	}

	@Inject(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V", at = @At("HEAD"), cancellable = true)
	private void filterBrokenToolModifiers(EquipmentSlotGroup slotGroup,
			BiConsumer<Holder<Attribute>, AttributeModifier> consumer, CallbackInfo callback) {
		filterBrokenToolModifiers(null, slotGroup, consumer, callback);
	}

	private void filterBrokenToolModifiers(EquipmentSlot slot, EquipmentSlotGroup slotGroup,
			BiConsumer<Holder<Attribute>, AttributeModifier> consumer, CallbackInfo callback) {
		ItemStack stack = (ItemStack) (Object) this;
		if(!(stack.getItem() instanceof IPsimetalTool tool) || IPsimetalTool.isEnabled(stack)) {
			return;
		}

		ItemAttributeModifiers modifiers = stack.getOrDefault(
				DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		BiConsumer<Holder<Attribute>, AttributeModifier> filtered = (attribute, modifier) -> {
			if(tool.keepsAttributeWhenBroken(attribute)) {
				consumer.accept(attribute, modifier);
			}
		};
		if(slot != null) {
			modifiers.forEach(slot, filtered);
		} else {
			modifiers.forEach(slotGroup, filtered);
		}
		callback.cancel();
	}
}
