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
import java.util.List;

public class PieceOperatorListUnion extends PieceOperator {

	SpellParam<EntityListWrapper> list1;
	SpellParam<EntityListWrapper> list2;

	public PieceOperatorListUnion(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list1 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
		addParam(list2 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		List<Entity> l1 = this.getNonnullParamValue(context, list1).unwrap();
		List<Entity> l2 = this.getNonnullParamValue(context, list2).unwrap();

		List<Entity> entities = new ArrayList<>(l1.size() + l2.size());
		int i = 0, j = 0;
		while (i < l1.size() && j < l2.size()) {
			int cmp = EntityListWrapper.compareEntities(l1.get(i), l2.get(j));
			if (cmp == 0) {
				i++;
				continue;
			}
			entities.add(cmp < 0 ? l1.get(i++) : l2.get(j++));
		}
		entities.addAll(l1.subList(i, l1.size()));
		entities.addAll(l2.subList(j, l2.size()));
		return new EntityListWrapper(entities);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}
}
