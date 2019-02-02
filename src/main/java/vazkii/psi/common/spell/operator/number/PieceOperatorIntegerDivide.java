/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [30/01/2016, 23:53:15 (GMT)]
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorIntegerDivide extends PieceOperator {

	SpellParam num1;
	SpellParam num2;

	public PieceOperatorIntegerDivide(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d1 = this.<Double>getParamValue(context, num1);
		Double d2 = this.<Double>getParamValue(context, num2);

		if(d2 == 0 || d2.intValue() == 0)
			throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);

		return (double) (int) (d1 / d2);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
