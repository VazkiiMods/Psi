/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public final class PsiArmorHandler {
	private PsiArmorHandler() {}

	public static void onEvent(PsiArmorEvent event) {
		if(event.getEntity().isSpectator()) {
			return;
		}

		for(ItemStack armor : event.getEntity().getInventory().armor) {
			if(!armor.isEmpty() && armor.getItem() instanceof IPsiEventArmor handler) {
				handler.onEvent(armor, event);
			}
		}
	}
}
