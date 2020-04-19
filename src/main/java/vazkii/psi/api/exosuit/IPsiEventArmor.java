/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.exosuit;

import net.minecraft.item.ItemStack;

/**
 * An ItemArmor that implements this can have stuff happen when a PsiArmorEvent happens.
 */
public interface IPsiEventArmor {

	void onEvent(ItemStack stack, PsiArmorEvent event);

}
