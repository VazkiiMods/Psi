/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public abstract class ItemExosuitSensor extends Item implements IExosuitSensor {

	// This should be modifiable, for the purposes of cosmetic addons like Magical Psi.
	public static int defaultColor = ICADColorizer.DEFAULT_SPELL_COLOR;
	public static int lightColor = 0xFFEC13;
	public static int underwaterColor = 0x1350FF;
	public static int fireColor = 0xFF1E13;
	public static int lowHealthColor = 0xFF8CC5;

	public ItemExosuitSensor(Item.Properties properties) {
		super(properties.maxStackSize(1));
	}

	@Override
	public String getEventType(ItemStack stack) {
		return PsiArmorEvent.NONE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		return defaultColor;
	}

}
