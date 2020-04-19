/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.infusion;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

public class PieceTrickGreaterInfusion extends PieceTrickInfusion {
	public PieceTrickGreaterInfusion(Spell spell) {
		super(spell);
	}

	@Override
	protected void addPotencyAndCost(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 250);
		meta.addStat(EnumSpellStat.COST, 2600);
	}

	@Override
	public boolean canCraft(PieceCraftingTrick trick) {
		return trick instanceof PieceTrickGreaterInfusion;
	}
}
