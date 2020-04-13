/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [25/01/2016, 20:16:02 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

public class PieceTrickInfusion extends PieceCraftingTrick {
	public PieceTrickInfusion(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		addPotencyAndCost(meta);
	}

	protected void addPotencyAndCost(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 100);
		meta.addStat(EnumSpellStat.COST, 1200);
	}

    @Override
	public boolean canCraft(PieceCraftingTrick trick) {
		return trick instanceof PieceTrickInfusion;
	}
}
