/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [11/03/2016, 20:22:18 (GMT)]
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorPower extends PieceOperator {

	SpellParam num;
	SpellParam power;

	public PieceOperatorPower(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.GREEN, false, false));
		addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d = this.<Double>getParamValue(context, num);
		Double p = this.<Double>getParamValue(context, power);

		int steps = p.intValue();
		if(steps < 0)
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		
		double dv = 1;
		for(int i = 0; i < steps; i++)
			dv *= d;
		
		return dv;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
