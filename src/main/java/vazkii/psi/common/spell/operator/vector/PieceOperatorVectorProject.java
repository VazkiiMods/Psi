/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
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
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorProject extends PieceOperator {

	SpellParam<Vector3> target;
	SpellParam<Vector3> axis;

	public PieceOperatorVectorProject(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.RED, false, false));
		addParam(axis = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) {
		Vector3 targetVal = this.getParamValue(context, target);
		Vector3 axisVal = this.getParamValue(context, axis);

		return targetVal.copy().project(axisVal);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
