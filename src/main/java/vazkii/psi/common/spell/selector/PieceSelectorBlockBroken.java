/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 21:04:39 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorBlockBroken extends PieceSelector {

	public PieceSelectorBlockBroken(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.positionBroken == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        return Vector3.fromVec3d(context.positionBroken.getHitVec());
    }

}
