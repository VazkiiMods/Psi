/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class PieceSelectorCasterEnergy extends PieceSelector {

	public PieceSelectorCasterEnergy(Spell spell) {
		super(spell);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
		return 1.0 * data.availablePsi;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
