/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.constant;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.interval.Interval;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamNumber;

public class PieceConstantWrapper extends SpellPiece {

	SpellParam<Number> target;
	SpellParam<Number> max;

	public PieceConstantWrapper(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(1));
	}

	@Override
	public void initParams() {
		addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false));
		addParam(max = new ParamNumber("psi.spellparam.constant", SpellParam.GREEN, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double targetVal = this.getParamValue(context, target).doubleValue();
		double maxVal = this.getParamValue(context, max).doubleValue();

		if(maxVal > 0) {
			return Math.min(maxVal, Math.abs(targetVal));
		} else if(maxVal < 0) {
			return Math.max(maxVal, -Math.abs(targetVal));
		} else {
			return 0.0;
		}
	}

	@Override
	public @NotNull Interval<?> evaluate() throws SpellCompilationException {
		IntervalNumber absValue = this.<Number, IntervalNumber>getNonNullParamEvaluation(target).abs();
		IntervalNumber bound = getNonNullParamEvaluation(max);
		if (bound.min > 0) bound = IntervalNumber.fromRange(0, bound.max);
		if (bound.max < 0) bound = IntervalNumber.fromRange(bound.min, 0);
		return IntervalNumber.fromRange(-absValue.max, absValue.max).clamp(bound);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONSTANT;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
