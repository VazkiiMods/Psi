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

public class PieceOperatorIntegerDivide extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num1;
	ParamNumberOrVector num2;
	ParamNumberOrVector num3;

	public PieceOperatorIntegerDivide(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
		addParam(num3 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.YELLOW, true, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		NumberOrVector.BinaryOp op = (a, b) -> {
			if(b == 0 || (int) b == 0) {
				throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
			}

			return a / b;
		};
		NumberOrVector quotient = getNumberOrVector(context, num1).combine(op, getNumberOrVector(context, num2));
		NumberOrVector third = getNumberOrVector(context, num3);
		if(third != null) {
			quotient = quotient.combine(op, third);
		}

		return quotient.map(d -> d < 0 ? Math.ceil(d) : Math.floor(d));
	}

}
