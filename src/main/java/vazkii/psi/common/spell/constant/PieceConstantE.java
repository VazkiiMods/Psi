/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [11/03/2016, 20:18:59 (GMT)]
 */
package vazkii.psi.common.spell.constant;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

public class PieceConstantE extends SpellPiece {

	public PieceConstantE(Spell spell) {
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
		return Math.E;
	}

	@Override
	public Object execute(SpellContext context) {
		return evaluate();
	}

}
