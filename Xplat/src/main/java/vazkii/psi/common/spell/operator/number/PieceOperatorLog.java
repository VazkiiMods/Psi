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

public class PieceOperatorLog extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num;
	ParamNumberOrVector base;

	public PieceOperatorLog(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumberOrVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
		addParam(base = new ParamNumberOrVector(SpellParam.GENERIC_NAME_BASE, SpellParam.RED, true, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		NumberOrVector.UnaryOp log = d -> {
			if(d < 0) {
				throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
			}

			return Math.log10(d);
		};

		NumberOrVector value = getNumberOrVector(context, num);
		NumberOrVector b = getNumberOrVector(context, base);
		if(b == null) {
			return value.map(log);
		}

		return value.combine((d, bd) -> log.apply(d) / log.apply(bd), b);
	}

}
