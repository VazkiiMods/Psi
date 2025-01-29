/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorModels {
	public static ModelArmor get(EquipmentSlot equipmentSlot) {
		return new ModelArmor(Minecraft.getInstance().getEntityModels().bakeLayer(equipmentSlot == EquipmentSlot.LEGS ? ModModelLayers.PSIMETAL_EXOSUIT_INNER_ARMOR : ModModelLayers.PSIMETAL_EXOSUIT_OUTER_ARMOR), equipmentSlot);
	}
}
