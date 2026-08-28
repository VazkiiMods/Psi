/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
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

public class PieceOperatorVectorRotate extends PieceOperator {

	private SpellParam<Vector3> vector;
	private SpellParam<Vector3> axis;
	private SpellParam<Number> angle;

	public PieceOperatorVectorRotate(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(vector = new ParamVector(SpellParam.GENERIC_NAME_VECTOR, SpellParam.RED, false, false));
		addParam(axis = new ParamVector(SpellParam.GENERIC_NAME_AXIS, SpellParam.CYAN, false, false));
		addParam(angle = new ParamNumber(SpellParam.GENERIC_NAME_ANGLE, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 v = this.getParamValue(context, vector);
		Vector3 a = this.getParamValue(context, axis);
		double an = this.getParamValue(context, angle).doubleValue();

		return v.copy().rotate(an, a.copy());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
