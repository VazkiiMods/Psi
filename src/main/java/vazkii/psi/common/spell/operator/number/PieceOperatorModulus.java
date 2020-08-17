/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.math.BigDecimal;

public class PieceOperatorModulus extends PieceOperator {

	SpellParam<Number> num1;
	SpellParam<Number> num2;

	public PieceOperatorModulus(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d1 = this.getParamValue(context, num1).doubleValue();
		double d2 = this.getParamValue(context, num2).doubleValue();

		if (d2 == 0) {
			throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
		}

		BigDecimal precise1 = new BigDecimal(d1);
		BigDecimal precise2 = new BigDecimal(d2);

		return precise1.remainder(precise2).doubleValue();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
