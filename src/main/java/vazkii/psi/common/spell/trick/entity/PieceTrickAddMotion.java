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
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.AdditiveMotionHandler;

public class PieceTrickAddMotion extends PieceTrick {

	public static final double MULTIPLIER = 0.3;

	SpellParam<Entity> target;
	SpellParam<Vector3> direction;
	SpellParam<Number> speed;

	public PieceTrickAddMotion(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel("psi.spellparam.speed", true).abs().mul(50));
		setStatLabel(EnumSpellStat.COST, new StatLabel("psi.spellparam.speed", true).abs().mul(90).sub(new StatLabel(3).mul("psi.statlabel.is_last", true).parenthesize()).max(1));
	}

	public static void addMotion(SpellContext context, Entity e, Vector3 dir, double speed) throws SpellRuntimeException {
		context.verifyEntity(e);
		if(!context.isInRadius(e)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		dir = dir.copy().normalize().multiply(MULTIPLIER * speed);

		if(Math.abs(dir.y) > 0.0001) {
			if(e.getDeltaMovement().y() + dir.y >= 0) {
				e.fallDistance = 0;
			} else if(dir.y > 0) {
				double magicnumber = 25d / 98d; // Equal to 1/terminal velocity of living entity
				double yvel = (e.getDeltaMovement().y() + dir.y) * magicnumber + 1; // inverse % of terminal velocity
				if(yvel > 0) {
					float newfall = (float) (-(49 / magicnumber) + (((49 * yvel) - (Math.log(yvel) / Math.log(4 * magicnumber))) / magicnumber));
					e.fallDistance = Math.min(e.fallDistance, Math.max(0, newfall));
				}
			}
		}

		AdditiveMotionHandler.addMotion(e, dir.x, dir.y, dir.z);

	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
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
		int dc = 0;
		if(!meta.getFlag("psi.addmotion")) {
			meta.setFlag("psi.addmotion", true);
			dc = 3;
		}
		meta.addStat(EnumSpellStat.POTENCY, (int) (absSpeed * 50));
		meta.addStat(EnumSpellStat.COST, (int) Math.max(1, absSpeed * 90 - dc));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		Vector3 directionVal = this.getParamValue(context, direction);
		double speedVal = this.getParamValue(context, speed).doubleValue();

		addMotion(context, targetVal, directionVal, speedVal);

		return null;
	}

}
