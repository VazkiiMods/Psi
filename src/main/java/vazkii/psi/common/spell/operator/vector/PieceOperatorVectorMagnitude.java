/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.vector;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorMagnitude extends PieceOperator {

	SpellParam<Vector3> vec1;

	public PieceOperatorVectorMagnitude(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(vec1 = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() {
		return IntervalNumber.fromRange(0, Double.POSITIVE_INFINITY);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 v1 = this.getParamValue(context, vec1);

		return v1.mag();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
