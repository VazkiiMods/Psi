/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

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
		EntityListWrapper listVal = this.getParamValueOrDefault(context, list, EntityListWrapper.EMPTY);

		if(targetVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return EntityListWrapper.withAdded(listVal, targetVal);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
