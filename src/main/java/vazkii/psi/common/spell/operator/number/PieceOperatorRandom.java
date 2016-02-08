/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [06/02/2016, 18:43:53 (GMT)]
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorRandom extends PieceOperator {

	SpellParam num;
	
	public PieceOperatorRandom(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d = this.<Double>getParamValue(context, num);

		boolean neg = d < 0;
		int i = Math.abs(d.intValue());
		if(i == 0)
			throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
		
		int v = context.caster.worldObj.rand.nextInt(i);
		if(neg)
			v = -v;
		
		return Double.valueOf(v);
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
