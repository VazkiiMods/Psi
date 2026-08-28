/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorCasterBattery extends PieceSelector {

	public PieceSelectorCasterBattery(Spell spell) {
		super(spell);
	}

	@Override
	public Object execute(SpellContext context) {
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		if(cad != null && cad.getItem() instanceof ICAD icad) {
			return icad.getStatValue(cad, EnumCADStat.OVERFLOW) * 1.0;
		}
		return 0.0;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
