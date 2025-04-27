/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number.trig;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorCos extends PieceOperator {

	SpellParam<Number> num;

	public PieceOperatorCos(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() throws SpellCompilationException {
		IntervalNumber iv = getNonNullParamEvaluation(num);
		return iv.periodicFn(i -> i.subtract(Math.PI).abs().preservingMonotonicMap(v -> Math.cos(v + Math.PI)), 2 * Math.PI, false);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d = this.getParamValue(context, num).doubleValue();

		return Math.cos(d);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
