/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorSubtract extends PieceOperator {

	SpellParam<Number> num1;
	SpellParam<Number> num2;
	SpellParam<Number> num3;

	public PieceOperatorSubtract(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() throws SpellCompilationException {
		return this.<Number, IntervalNumber>getNonNullParamEvaluation(num1).subtract(getNonNullParamEvaluation(num2)).subtract(getParamEvaluationeOrDefault(num3, IntervalNumber.zero));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d1 = this.getParamValue(context, num1).doubleValue();
		double d2 = this.getParamValue(context, num2).doubleValue();
		Number d3 = this.getParamValue(context, num3);
		if(d3 == null) {
			d3 = 0D;
		}

		return d1 - d2 - d3.doubleValue();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
