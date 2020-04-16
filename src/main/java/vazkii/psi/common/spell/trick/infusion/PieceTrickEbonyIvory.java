/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 19:37:26 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

public class PieceTrickEbonyIvory extends PieceTrickGreaterInfusion {
	public PieceTrickEbonyIvory(Spell spell) {
		super(spell);
	}

	@Override
	protected void addPotencyAndCost(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 250);
		meta.addStat(EnumSpellStat.COST, 3000);
	}

	@Override
	public boolean canCraft(PieceCraftingTrick trick) {
		return trick instanceof PieceTrickEbonyIvory;
	}
}
