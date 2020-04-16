/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 19:33:34 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.api.spell.piece.PieceTrick;

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
