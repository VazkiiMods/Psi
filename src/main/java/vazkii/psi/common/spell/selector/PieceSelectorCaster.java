/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.entity.player.PlayerEntity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorCaster extends PieceSelector {

	public PieceSelectorCaster(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return PlayerEntity.class;
	}

	@Override
	public Object execute(SpellContext context) {
		return context.caster;
	}

}
