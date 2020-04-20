/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceOperatorListAdd extends PieceOperator {

	SpellParam<Entity> target;
	SpellParam<EntityListWrapper> list;

	public PieceOperatorListAdd(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
		addParam(list = new ParamEntityListWrapper("psi.spellparam.list", SpellParam.YELLOW, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		EntityListWrapper listVal = this.getParamValue(context, list);

		if (targetVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		List<Entity> list = new ArrayList<>(listVal.unwrap());
		int index = Collections.binarySearch(list, targetVal, PsiAPI::compareEntities);
		if (index < 0) {
			list.add(~index, targetVal);
		}

		return new EntityListWrapper(list);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
