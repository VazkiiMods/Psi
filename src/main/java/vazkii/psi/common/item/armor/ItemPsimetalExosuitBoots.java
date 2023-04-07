/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemPsimetalExosuitBoots extends ItemPsimetalArmor {

	public ItemPsimetalExosuitBoots(EquipmentSlot slotType, Item.Properties properties) {
		super(slotType, properties);
	}

	@Override
	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.JUMP;
	}

}
