package vazkii.psi.common.spell.operator.vector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorMaximum extends PieceOperator {
	SpellParam<Vector3> vec1;
	SpellParam<Vector3> vec2;

	public PieceOperatorVectorMaximum(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(vec1 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.GREEN, false, false));
		addParam(vec2 = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.GREEN, false, false));

	}

	@Override
	public Object execute(SpellContext context) {
		Vector3 v1 = this.getParamValue(context, vec1);
		Vector3 v2 = this.getParamValue(context, vec2);
		Vector3 newVector = Vector3.zero;
		newVector.x = Math.max(v1.x, v2.x);
		newVector.y = Math.max(v1.y, v2.y);
		newVector.z = Math.max(v1.z, v2.z);

		return newVector;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
