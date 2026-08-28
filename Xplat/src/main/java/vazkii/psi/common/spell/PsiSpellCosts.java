/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellAcceptor;

public final class PsiSpellCosts {

	private PsiSpellCosts() {}

	public static int realCost(ItemStack cadStack, ItemStack bullet, int cost) {
		if(cadStack.isEmpty() || !(cadStack.getItem() instanceof ICAD cad)) {
			return cost;
		}

		int efficiency = cad.getStatValue(cadStack, EnumCADStat.EFFICIENCY);
		if(efficiency == -1) {
			return -1;
		}
		if(efficiency == 0) {
			return cost;
		}

		double processedCost = cost / ((double) efficiency / 100);
		if(!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet)) {
			processedCost *= ISpellAcceptor.acceptor(bullet).getCostModifier();
		}
		return (int) processedCost;
	}

}
