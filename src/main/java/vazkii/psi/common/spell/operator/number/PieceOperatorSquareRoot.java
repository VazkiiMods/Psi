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

public class PieceOperatorSquareRoot extends PieceOperator {

	SpellParam<Number> num;

	public PieceOperatorSquareRoot(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() throws SpellCompilationException {
		IntervalNumber iv = getNonNullParamEvaluation(num);
		return iv.preservingMonotonicMap(Math::sqrt);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d = this.getParamValue(context, num).doubleValue();

		if(d < 0) {
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		}

		return Math.sqrt(d);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
