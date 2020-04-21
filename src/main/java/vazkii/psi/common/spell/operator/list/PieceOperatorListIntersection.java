/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.list;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceOperatorListIntersection extends PieceOperator {

	SpellParam<EntityListWrapper> list1;
	SpellParam<EntityListWrapper> list2;

	public PieceOperatorListIntersection(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list1 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
		addParam(list2 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper l1 = this.getNonnullParamValue(context, list1);
		EntityListWrapper l2 = this.getNonnullParamValue(context, list2);

		List<Entity> list = new ArrayList<>();
		List<Entity> search = l2.unwrap();
		for (Entity e : l1) {
			if (Collections.binarySearch(search, e, EntityListWrapper::compareEntities) >= 0) {
				list.add(e);
			}
		}

		return new EntityListWrapper(list);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
