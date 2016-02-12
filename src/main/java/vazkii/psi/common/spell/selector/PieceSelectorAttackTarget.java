/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 21:21:54 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.entity.EntityLivingBase;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorAttackTarget extends PieceSelector {

	public PieceSelectorAttackTarget(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityLivingBase.class;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.attackedEntity == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

		return context.attackedEntity;
	}

}
