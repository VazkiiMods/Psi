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

public class PieceOperatorIntegerDivide extends PieceOperator {

	SpellParam<Number> num1;
	SpellParam<Number> num2;
	SpellParam<Number> num3;

	public PieceOperatorIntegerDivide(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.YELLOW, true));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() throws SpellCompilationException {
		IntervalNumber i1 = getNonNullParamEvaluation(num1);
		IntervalNumber i2 = getNonNullParamEvaluation(num2);
		IntervalNumber i3 = getParamEvaluation(num3);
		IntervalNumber iv = i3 == null ? i1.divide(i2) : i1.divide(i2.multiply(i3));
		return iv.preservingMonotonicMap(v -> v < 0 ? Math.ceil(v) : Math.floor(v));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d1 = this.getParamValue(context, num1).doubleValue();
		Number d2 = this.getParamValue(context, num2).doubleValue();
		Number d3 = this.getParamValue(context, num3);

		if((d2.doubleValue() == 0 || d2.intValue() == 0) || (d3 != null && (d3.doubleValue() == 0 || d3.intValue() == 0))) {
			throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
		}
		Double d4 = d3 != null ? (d1 / (d2.doubleValue() * d3.doubleValue())) : (d1 / d2.doubleValue());
		if(d4 < 0) {
			return Math.ceil(d4);
		}
		return Math.floor(d4);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
