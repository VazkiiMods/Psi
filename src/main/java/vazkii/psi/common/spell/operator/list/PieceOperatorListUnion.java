/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.list;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListUnion extends PieceOperator {

	SpellParam<EntityListWrapper> list1;
	SpellParam<EntityListWrapper> list2;

	public PieceOperatorListUnion(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list1 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST1, SpellParam.BLUE, false));
		addParam(list2 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST2, SpellParam.RED, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper l1 = this.getNonnullParamValue(context, list1);
		EntityListWrapper l2 = this.getNonnullParamValue(context, list2);

		return EntityListWrapper.union(l1, l2);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}
}
