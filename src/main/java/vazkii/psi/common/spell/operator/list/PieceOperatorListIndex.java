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

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListIndex extends PieceOperator {

	SpellParam<EntityListWrapper> list;
	SpellParam<Number> number;

	public PieceOperatorListIndex(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST, SpellParam.CYAN, false, false));
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int num = this.getNonnullParamValue(context, number).intValue();
		EntityListWrapper listVal = this.getNonnullParamValue(context, list);

		if (num >= 0 && num < listVal.unwrap().size()) {
			return listVal.unwrap().get(num);
		} else {
			throw new SpellRuntimeException(SpellRuntimeException.OUT_OF_BOUNDS);
		}
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}
}
