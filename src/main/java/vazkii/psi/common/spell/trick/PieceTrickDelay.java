/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.spell.*;
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
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);

		Double timeVal = this.<Double>getParamEvaluation(time);
		if(timeVal == null || timeVal <= 0 || timeVal != timeVal.intValue()) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, timeVal.intValue());
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		context.delay = this.getParamValue(context, time).intValue();

		return null;
	}

}
