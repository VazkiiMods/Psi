/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorLoopcastIndex extends PieceSelector {

	public PieceSelectorLoopcastIndex(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() {
		return IntervalNumber.fromRange(0, Double.POSITIVE_INFINITY);
	}

	@Override
	public Object execute(SpellContext context) {
		return (double) context.loopcastIndex;
	}

}
