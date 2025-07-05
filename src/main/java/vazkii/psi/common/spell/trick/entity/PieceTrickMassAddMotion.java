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
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceTrickMassAddMotion extends PieceTrick {

	SpellParam<EntityListWrapper> target;
	SpellParam<Vector3> direction;
	SpellParam<Number> speed;

	public PieceTrickMassAddMotion(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel("psi.spellparam.speed", true).abs().mul(90));
		setStatLabel(EnumSpellStat.COST, new StatLabel("psi.spellparam.speed", true).abs().mul(105).max(1));
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(direction = new ParamVector("psi.spellparam.direction", SpellParam.GREEN, false, false));
		addParam(speed = new ParamNumber("psi.spellparam.speed", SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double speedVal = this.<Double>getParamEvaluation(speed);
		if(speedVal == null) {
			speedVal = 1D;
		}

		double absSpeed = Math.abs(speedVal);
		meta.addStat(EnumSpellStat.POTENCY, (int) (absSpeed * 90));
		meta.addStat(EnumSpellStat.COST, (int) Math.max(1, absSpeed * 105));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper targetVal = this.getParamValue(context, target);
		Vector3 directionVal = this.getParamValue(context, direction);
		double speedVal = this.getParamValue(context, speed).doubleValue();

		for(Entity e : targetVal) {
			PieceTrickAddMotion.addMotion(context, e, directionVal, speedVal);
		}

		return null;
	}

}
