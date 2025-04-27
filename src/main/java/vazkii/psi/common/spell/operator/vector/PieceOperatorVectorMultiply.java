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
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorMultiply extends PieceOperator {

	SpellParam<Vector3> vec1;
	SpellParam<Number> num2;

	public PieceOperatorVectorMultiply(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(vec1 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.RED, false, false));
		addParam(num2 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 v1 = this.getParamValue(context, vec1);
		double d = this.getParamValue(context, num2).doubleValue();

		return v1.copy().multiply(d);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
