/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 17:34:28 (GMT)]
 */
package vazkii.psi.common.spell.operator.number.trig;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorAsin extends PieceOperator {

	SpellParam num;

	public PieceOperatorAsin(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d = this.<Double>getParamValue(context, num);
		if(d < -1 || d > 1)
			throw new SpellRuntimeException("psi.spellerror.outsidetrigdomain");

		return Math.asin(d);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
