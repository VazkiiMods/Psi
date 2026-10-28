/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.NumberOrVector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumberOrVector;
import vazkii.psi.api.spell.piece.PieceOperatorNumberOrVector;

public class PieceOperatorMin extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num1;
	ParamNumberOrVector num2;
	ParamNumberOrVector num3;

	public PieceOperatorMin(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false, false));
		addParam(num2 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
		addParam(num3 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		NumberOrVector.BinaryOp op = Math::min;
		NumberOrVector result = getNumberOrVector(context, num1).combine(op, getNumberOrVector(context, num2));
		NumberOrVector third = getNumberOrVector(context, num3);
		return third == null ? result : result.combine(op, third);
	}

}
