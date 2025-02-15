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

public class PieceOperatorRandom extends PieceOperator {

	SpellParam<Number> max;
	SpellParam<Number> min;

	public PieceOperatorRandom(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.BLUE, false, false));
		addParam(min = new ParamNumber(SpellParam.GENERIC_NAME_MIN, SpellParam.RED, true, false));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() throws SpellCompilationException {
		IntervalNumber min = this.getParamEvaluationeOrDefault(this.min, IntervalNumber.zero);
		IntervalNumber max = this.getNonNullParamEvaluation(this.max);
		return IntervalNumber.fromRange(min.min, max.max);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int maxVal = this.getParamValue(context, max).intValue();
		int minVal = this.getParamValueOrDefault(context, min, 0).intValue();

		if(maxVal - minVal <= 0) {
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		}

		return (double) (context.caster.getCommandSenderWorld().random.nextInt(maxVal - minVal) + minVal);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
