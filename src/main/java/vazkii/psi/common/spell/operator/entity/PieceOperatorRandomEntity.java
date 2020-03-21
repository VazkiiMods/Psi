/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 00:57:43 (GMT)]
 */
package vazkii.psi.common.spell.operator.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

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
		if(entities.size() == 0)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

		return entities.get(context.caster.getEntityWorld().rand.nextInt(entities.size()));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
