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

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;

public class PieceOperatorRandomEntity extends PieceOperator {

	SpellParam<EntityListWrapper> list;

	public PieceOperatorRandomEntity(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper listVal = this.getParamValue(context, list);
		List<Entity> entities = listVal.unwrap();
		if (entities.size() == 0) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return entities.get(context.caster.getEntityWorld().rand.nextInt(entities.size()));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
