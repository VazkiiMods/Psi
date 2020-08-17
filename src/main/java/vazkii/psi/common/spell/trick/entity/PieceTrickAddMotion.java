/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.AdditiveMotionHandler;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageAdditiveMotion;

public class PieceTrickAddMotion extends PieceTrick {

	public static final double MULTIPLIER = 0.3;

	SpellParam<Entity> target;
	SpellParam<Vector3> direction;
	SpellParam<Number> speed;

	public PieceTrickAddMotion(Spell spell) {
		super(spell);
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
		if (speedVal == null) {
			speedVal = 1D;
		}

		double absSpeed = Math.abs(speedVal);
		meta.addStat(EnumSpellStat.POTENCY, (int) multiplySafe(absSpeed, absSpeed, 3.5));
		meta.addStat(EnumSpellStat.COST, (int) multiplySafe(absSpeed, Math.max(1, absSpeed), 100));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		Vector3 directionVal = this.getParamValue(context, direction);
		double speedVal = this.getParamValue(context, speed).doubleValue();

		addMotion(context, targetVal, directionVal, speedVal);

		return null;
	}

	public static void addMotion(SpellContext context, Entity e, Vector3 dir, double speed) throws SpellRuntimeException {
		context.verifyEntity(e);
		if (!context.isInRadius(e)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		dir = dir.copy().normalize().multiply(MULTIPLIER * speed);

		String key = "psi:Entity" + e.getEntityId() + "Motion";

		double x = 0;
		double y = 0;
		double z = 0;

		if (Math.abs(dir.x) > 0.0001) {
			String keyv = key + "X";
			if (!context.customData.containsKey(keyv)) {
				x += dir.x;
				context.customData.put(keyv, 0);
			}
		}

		if (Math.abs(dir.y) > 0.0001) {
			String keyv = key + "Y";
			if (!context.customData.containsKey(keyv)) {
				y += dir.y;
				context.customData.put(keyv, 0);
			}

			if (e.getMotion().getY() >= 0) {
				e.fallDistance = 0;
			}
		}

		if (Math.abs(dir.z) > 0.0001) {
			String keyv = key + "Z";
			if (!context.customData.containsKey(keyv)) {
				z += dir.z;
				context.customData.put(keyv, 0);
			}
		}

		if (e instanceof ServerPlayerEntity) {
			MessageAdditiveMotion motion = new MessageAdditiveMotion(e.getEntityId(), x, y, z);
			MessageRegister.sendToPlayer(motion, (ServerPlayerEntity) e);
		} else {
			AdditiveMotionHandler.addMotion(e, x, y, z);
		}

	}

}
