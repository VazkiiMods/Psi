/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [11/03/2016, 20:28:19 (GMT)]
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorLog extends PieceOperator {

	SpellParam num;
	SpellParam base;
	
	public PieceOperatorLog(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
		addParam(base = new ParamNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.RED, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d = this.<Double>getParamValue(context, num);

		if(d < 0)
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		
		double logNum = Math.log10(d);
		
		Double b = this.<Double>getParamValue(context, base);
		if(b != null) {
			if(b < 0)
				throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
			
			logNum /= Math.log10(b);
		}
		
		return logNum;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
