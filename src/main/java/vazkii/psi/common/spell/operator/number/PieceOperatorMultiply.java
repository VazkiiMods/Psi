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
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorMultiply extends PieceOperator {

	SpellParam<Number> num1;
	SpellParam<Number> num2;
	SpellParam<Number> num3;

	public PieceOperatorMultiply(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true, false));
	}

	@Override
	public Object execute(SpellContext context) {
		double d1 = this.getParamValue(context, num1).doubleValue();
		double d2 = this.getParamValue(context, num2).doubleValue();
		Number d3 = this.getParamValue(context, num3);
		if (d3 == null) {
			d3 = 1D;
		}

		return d1 * d2 * d3.doubleValue();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
