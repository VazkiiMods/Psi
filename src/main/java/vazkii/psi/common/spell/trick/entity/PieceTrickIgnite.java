/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.world.entity.Entity;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickIgnite extends PieceTrick {

	SpellParam<Entity> target;
	SpellParam<Number> time;

	public PieceTrickIgnite(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).abs().mul(40));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).abs().mul(65));
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double timeVal = this.<Double>getParamEvaluation(time);

		if(timeVal == null || timeVal <= 0 || timeVal != timeVal.intValue()) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, timeVal.intValue() * 40);
		meta.addStat(EnumSpellStat.COST, timeVal.intValue() * 65);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		int timeVal = this.getParamValue(context, time).intValue();

		context.verifyEntity(targetVal);
		targetVal.igniteForSeconds(timeVal);

		return null;
	}

}
