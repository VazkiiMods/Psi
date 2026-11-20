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

import java.math.BigDecimal;

public class PieceOperatorModulus extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num1;
	ParamNumberOrVector num2;

	public PieceOperatorModulus(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		return getNumberOrVector(context, num1).combine((a, b) -> {
			if(b == 0) {
				throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
			}

			return new BigDecimal(a).remainder(new BigDecimal(b)).doubleValue();
		}, getNumberOrVector(context, num2));
	}

}
