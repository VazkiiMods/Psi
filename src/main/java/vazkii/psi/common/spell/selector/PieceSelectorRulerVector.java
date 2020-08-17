/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.item.ItemVectorRuler;

public class PieceSelectorRulerVector extends PieceSelector {

	public PieceSelectorRulerVector(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

	@Override
	public Object execute(SpellContext context) {
		return ItemVectorRuler.getRulerVector(context.caster);
	}

}
