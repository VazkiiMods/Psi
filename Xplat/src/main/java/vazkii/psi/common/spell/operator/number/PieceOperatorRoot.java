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

public class PieceOperatorRoot extends PieceOperatorNumberOrVector {

	ParamNumberOrVector num;
	ParamNumberOrVector root;

	public PieceOperatorRoot(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumberOrVector(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false, false));
		addParam(root = new ParamNumberOrVector(SpellParam.GENERIC_NAME_ROOT, SpellParam.RED, false, false));
	}

	@Override
	protected NumberOrVector compute(SpellContext context) throws SpellRuntimeException {
		return getNumberOrVector(context, num).combine((b, r) -> {
			if(b < 0 && r % 2 == 0) {
				throw new SpellRuntimeException(SpellRuntimeException.EVEN_ROOT_NEGATIVE_NUMBER);
			}

			return Math.pow(b, 1.0 / r);
		}, getNumberOrVector(context, root));
	}
}
