/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
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

	SpellParam<Number> num1;
	SpellParam<Number> num2;
	SpellParam<Number> num3;

	public PieceOperatorVectorConstruct(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_X, SpellParam.RED, true, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_Y, SpellParam.GREEN, true, false));
		addParam(num3 = new ParamNumber(SpellParam.GENERIC_NAME_Z, SpellParam.BLUE, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Number d1 = this.getParamValue(context, num1);
		Number d2 = this.getParamValue(context, num2);
		Number d3 = this.getParamValue(context, num3);

		if(d1 == null) {
			d1 = 0D;
		}
		if(d2 == null) {
			d2 = 0D;
		}
		if(d3 == null) {
			d3 = 0D;
		}

		return new Vector3(d1.doubleValue(), d2.doubleValue(), d3.doubleValue());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
