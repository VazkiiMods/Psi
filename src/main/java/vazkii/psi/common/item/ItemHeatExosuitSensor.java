/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;

import vazkii.psi.api.exosuit.PsiArmorEvent;

import net.minecraft.item.Item.Properties;

public class ItemHeatExosuitSensor extends ItemExosuitSensor {

	public ItemHeatExosuitSensor(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		return ItemExosuitSensor.fireColor;
	}

	@Override
	public String getEventType(ItemStack stack) {
		return PsiArmorEvent.ON_FIRE;
	}
}
