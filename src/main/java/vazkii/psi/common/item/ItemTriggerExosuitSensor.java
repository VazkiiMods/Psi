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
import vazkii.psi.client.core.handler.ColorHandler;

public class ItemTriggerExosuitSensor extends ItemExosuitSensor {

	public ItemTriggerExosuitSensor(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		return ColorHandler.pulseColor(0xBC650F, 0.1f, 96);
	}

	@Override
	public String getEventType(ItemStack stack) {
		return PsiArmorEvent.DETONATE;
	}
}
