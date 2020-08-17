/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An item that implements this works as CAD colorizer, by which it can change
 * the CAD's spell color.
 */
public interface ICADColorizer extends ICADComponent {

	int DEFAULT_SPELL_COLOR = 0x13C5FF;

	/**
	 * Gets the color of the spells projected by the CAD that has
	 * this colorizer.
	 */
	@OnlyIn(Dist.CLIENT)
	int getColor(ItemStack stack);

	@Override
	default EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.DYE;
	}

	/**
	 * Gets the contributor name in the stack
	 * Used for contributor rewards
	 */
	String getContributorName(ItemStack stack);

	/**
	 * Sets the contributor name in the stack
	 * Used for contributor rewards
	 */
	void setContributorName(ItemStack stack, String name);
}
