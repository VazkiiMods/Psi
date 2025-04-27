/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDelay extends PieceTrick {

	SpellParam<Number> time;

	public PieceTrickDelay(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(2));
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_TIME, true));
	}

	@Override
	public void initParams() {
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);

		double timeVal = this.<Number, IntervalNumber>getNonNullParamEvaluation(time).max;
		if(timeVal < 1) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, (int) timeVal);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		context.delay = this.getParamValue(context, time).intValue();

		return null;
	}

}
