/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 17:26:14 (GMT)]
 */
package vazkii.psi.common.spell.constant;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

public class PieceConstantPi extends SpellPiece {

	public PieceConstantPi(Spell spell) {
		super(spell);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONSTANT;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

	@Override
	public Object evaluate() {
		return Math.PI;
	}

	@Override
	public Object execute(SpellContext context) {
		return evaluate();
	}

}
