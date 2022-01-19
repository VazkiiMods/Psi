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
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceTrickMassExodus extends PieceTrick {

	SpellParam<EntityListWrapper> target;
	SpellParam<Vector3> position;
	SpellParam<Number> speed;

	public PieceTrickMassExodus(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.GREEN, false, false));
		addParam(speed = new ParamNumber("psi.spellparam.speed", SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double speedVal = this.<Double>getParamEvaluation(speed);
		if (speedVal == null) {
			speedVal = 1D;
		}

		double absSpeed = Math.abs(speedVal);
		meta.addStat(EnumSpellStat.POTENCY, (int) (absSpeed * 100));
		meta.addStat(EnumSpellStat.COST, (int) Math.max(1, absSpeed * 100));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper targetVal = this.getParamValue(context, target);
		Vector3 positionVal = this.getParamValue(context, position);
		double speedVal = this.getParamValue(context, speed).doubleValue();

		for (Entity e : targetVal) {
			Vector3 vec = positionVal.copy().sub(Vector3.fromEntity(e));
			PieceTrickAddMotion.addMotion(context, e, vec, speedVal);
		}

		return null;
	}

}
