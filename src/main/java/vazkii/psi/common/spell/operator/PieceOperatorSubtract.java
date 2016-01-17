/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 19:18:25 (GMT)]
 */
package vazkii.psi.common.spell.operator;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamNumberConstant;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorSubtract extends PieceOperator {

	SpellParam num1;
	SpellParam num2;
	SpellParam num3;
	
	public PieceOperatorSubtract(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true));
	}
	
	@Override
	public Object execute(SpellContext context) {
		Double d1 = this.<Double>getParamValue(context, num1);
		Double d2 = this.<Double>getParamValue(context, num2);
		Double d3 = this.<Double>getParamValue(context, num3);
		if(d3 == null)
			d3 = 0D;
		
		return d1 - d2 - d3;
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
