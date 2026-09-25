/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.NumberOrVector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumberOrVector;
import vazkii.psi.api.spell.piece.PieceOperatorNumberOrVector;

import java.util.concurrent.ThreadLocalRandom;

public class PieceOperatorRandom extends PieceOperatorNumberOrVector {

	ParamNumberOrVector max;
	ParamNumberOrVector min;

	public PieceOperatorRandom(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(max = new ParamNumberOrVector(SpellParam.GENERIC_NAME_MAX, SpellParam.BLUE, false, false));
		addParam(min = new ParamNumberOrVector(SpellParam.GENERIC_NAME_MIN, SpellParam.RED, true, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		return getNumberOrVector(context, max).combine((mx, mn) -> {
			int maxVal = (int) mx;
			int minVal = (int) mn;
			if(maxVal - minVal <= 0) {
				throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
			}

			return ThreadLocalRandom.current().nextInt(maxVal - minVal) + minVal;
		}, NumberOrVector.of(this.getParamValueOrDefault(context, min, 0.0)));
	}

}
