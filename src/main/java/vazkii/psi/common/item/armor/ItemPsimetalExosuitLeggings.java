/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemPsimetalExosuitLeggings extends ItemPsimetalArmor {

	public ItemPsimetalExosuitLeggings(EquipmentSlotType slotType, Item.Properties properties) {
		super(slotType, properties);
	}

	@Override
	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.TICK;
	}

	@Override
	public int getCastCooldown(ItemStack stack) {
		return 0;
	}

	@Override
	public float getCastVolume() {
		return 0F;
	}

}
