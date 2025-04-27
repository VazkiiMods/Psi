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

public class PieceSelectorSneakStatus extends PieceSelector {

	public PieceSelectorSneakStatus(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

	@Override
	public @NotNull IntervalNumber evaluate() {
		return IntervalNumber.zeroToOne;
	}
	
	@Override
	public Object execute(SpellContext context) {
		return context.caster.isShiftKeyDown() ? 0D : 1D;
	}

}
