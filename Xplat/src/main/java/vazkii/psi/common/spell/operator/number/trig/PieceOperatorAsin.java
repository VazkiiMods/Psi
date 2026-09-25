/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number.trig;

import vazkii.psi.api.spell.NumberOrVector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumberOrVector;
import vazkii.psi.api.spell.piece.PieceOperatorNumberOrVector;

public class PieceOperatorAsin extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num;

	public PieceOperatorAsin(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumberOrVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		return getNumberOrVector(context, num).map(d -> {
			if(d < -1 || d > 1) {
				throw new SpellRuntimeException("psi.spellerror.outsidetrigdomain");
			}

			return Math.asin(d);
		});
	}

}
