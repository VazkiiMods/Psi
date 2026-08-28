/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.common.item.armor.ItemPsimetalArmor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ArmorModels {
	private static Map<EquipmentSlot, ModelArmor> exosuit = Collections.emptyMap();

	private static Map<EquipmentSlot, ModelArmor> make(EntityRendererProvider.Context ctx) {
		Map<EquipmentSlot, ModelArmor> ret = new EnumMap<>(EquipmentSlot.class);
		for(var slot : EquipmentSlot.values()) {
			var mesh = ctx.bakeLayer(slot == EquipmentSlot.LEGS ? ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR : ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR);
			ret.put(slot, new ModelArmor(mesh));
		}
		return ret;
	}

	public static void init(EntityRendererProvider.Context ctx) {
		exosuit = make(ctx);
	}

	@Nullable
	public static ModelArmor get(ItemStack stack) {
		Item item = stack.getItem();
		if(item instanceof ItemPsimetalArmor armor) {
			return exosuit.get(armor.getEquipmentSlot());
		}

		return null;
	}

	@Nullable
	public static ModelArmor prepare(ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> contextModel) {
		ModelArmor model = get(stack);
		if(model == null) {
			return null;
		}

		contextModel.copyPropertiesTo(model);
		model.setAllVisible(false);
		switch(slot) {
		case HEAD -> {
			model.head.visible = true;
			model.hat.visible = true;
		}
		case CHEST -> {
			model.body.visible = true;
			model.rightArm.visible = true;
			model.leftArm.visible = true;
		}
		case LEGS -> {
			model.body.visible = true;
			model.rightLeg.visible = true;
			model.leftLeg.visible = true;
		}
		case FEET -> {
			model.rightLeg.visible = true;
			model.leftLeg.visible = true;
		}
		default -> {
		}
		}
		return model;
	}
}
