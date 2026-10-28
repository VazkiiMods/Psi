/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.internal.math.Gamma;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumberOrVector;
import vazkii.psi.api.spell.piece.PieceOperatorNumberOrVector;

public class PieceOperatorGammaFunc extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num1;

	public PieceOperatorGammaFunc(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(1));
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		return getNumberOrVector(context, num1).map(d -> {
			if(d <= 0) {
				throw new SpellRuntimeException(SpellRuntimeException.NON_POSITIVE_VALUE);
			}

			return Gamma.gamma(d);
		});
	}
}
