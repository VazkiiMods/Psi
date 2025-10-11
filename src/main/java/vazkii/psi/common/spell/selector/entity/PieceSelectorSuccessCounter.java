/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.item.base.ModDataComponents;

public class PieceSelectorSuccessCounter extends PieceSelector {
	public PieceSelectorSuccessCounter(Spell spell) {
		super(spell);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(!(context.tool.getItem() instanceof IPsiEventArmor)) {
			throw new SpellRuntimeException(SpellRuntimeException.ARMOR);
		}
		return context.tool.getOrDefault(ModDataComponents.TIMES_CAST, 0) * 1.0;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
