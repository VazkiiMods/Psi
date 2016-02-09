/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [09/02/2016, 00:04:21 (GMT)]
 */
package vazkii.psi.common.spell.operator.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListAdd extends PieceOperator {

	SpellParam target;
	SpellParam list;
	
	public PieceOperatorListAdd(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
		addParam(list = new ParamEntityListWrapper("psi.spellparam.list", SpellParam.YELLOW, false, false));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);
		EntityListWrapper listVal = this.<EntityListWrapper>getParamValue(context, list);
		
		if(targetVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		
		List<Entity> list = new ArrayList(listVal.unwrap());
		if(!list.contains(targetVal))
			list.add(targetVal);
		
		return new EntityListWrapper(list);
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
