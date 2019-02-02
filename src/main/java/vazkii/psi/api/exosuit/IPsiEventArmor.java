/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/02/2016, 14:05:28 (GMT)]
 */
package vazkii.psi.api.exosuit;

import net.minecraft.item.ItemStack;

/**
 * An ItemArmor that implements this can have stuff happen when a PsiArmorEvent happens.
 */
public interface IPsiEventArmor {

	void onEvent(ItemStack stack, PsiArmorEvent event);

}
