/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.list;

import net.minecraft.world.entity.Entity;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListIndex extends PieceOperator {

	SpellParam<EntityListWrapper> list;
	SpellParam<Number> number;

	public PieceOperatorListIndex(Spell spell) {
		super(spell);

		setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(1));
	}

	@Override
	public void initParams() {
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST, SpellParam.CYAN, false, false));
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int num = this.getNonnullParamValue(context, number).intValue();
		EntityListWrapper listVal = this.getNonnullParamValue(context, list);

		if(num >= 0 && num < listVal.size()) {
			return listVal.get(num);
		} else {
			throw new SpellRuntimeException(SpellRuntimeException.OUT_OF_BOUNDS);
		}
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}
}
