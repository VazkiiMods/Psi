/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [20/01/2016, 15:47:39 (GMT)]
 */
package vazkii.psi.common.spell.operator.vector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorConstruct extends PieceOperator {

	SpellParam num1;
	SpellParam num2;
	SpellParam num3;
	
	public PieceOperatorVectorConstruct(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_X, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_Y, SpellParam.GREEN, false, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_Z, SpellParam.BLUE, false, false));	
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double d1 = this.<Double>getParamValue(context, num1);
		Double d2 = this.<Double>getParamValue(context, num2);
		Double d3 = this.<Double>getParamValue(context, num3);
		
		return new Vector3(d1, d2, d3);
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
