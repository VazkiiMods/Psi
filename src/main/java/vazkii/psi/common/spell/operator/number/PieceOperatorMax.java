/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 00:07:35 (GMT)]
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorMax extends PieceOperator {

	SpellParam num1;
	SpellParam num2;
	SpellParam num3;

	public PieceOperatorMax(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d1 = this.<Double>getParamValue(context, num1);
		Double d2 = this.<Double>getParamValue(context, num2);
		Double d3 = this.<Double>getParamValue(context, num3);
		if(d3 == null)
			d3 = Double.NEGATIVE_INFINITY;

		return Math.max(d1, Math.max(d2, d3));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
