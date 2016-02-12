/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/01/2016, 15:47:39 (GMT)]
 */
package vazkii.psi.common.spell.operator.vector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorSum extends PieceOperator {

	SpellParam vec1;
	SpellParam vec2;
	SpellParam vec3;

	public PieceOperatorVectorSum(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(vec1 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.GREEN, false, false));
		addParam(vec2 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.GREEN, false, false));
		addParam(vec3 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR3, SpellParam.GREEN, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 v1 = this.<Vector3>getParamValue(context, vec1);
		Vector3 v2 = this.<Vector3>getParamValue(context, vec2);
		Vector3 v3 = this.<Vector3>getParamValue(context, vec3);

		Vector3 r = v1.copy().add(v2);
		if(v3 != null)
			r.add(v3);

		return r;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
