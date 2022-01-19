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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageBlink;

public class PieceTrickBlink extends PieceTrick {

	SpellParam<Entity> target;
	SpellParam<Number> distance;

	public PieceTrickBlink(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double distanceVal = this.<Double>getParamEvaluation(distance);
		if (distanceVal == null) {
			distanceVal = 1D;
		}

		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 30));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 40));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		double distanceVal = this.getParamValue(context, distance).doubleValue();

		blink(context, targetVal, distanceVal);

		return null;
	}

	public static void blink(SpellContext context, Entity e, double dist) throws SpellRuntimeException {
		context.verifyEntity(e);
		if (!context.isInRadius(e)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		Vec3 look = e.getLookAngle();

		double offX = look.x * dist;
		double offY = e.equals(context.caster) ? look.y * dist : Math.max(0, look.y * dist);
		double offZ = look.z * dist;

		e.setPos(e.getX() + offX, e.getY() + offY, e.getZ() + offZ);
		if (e instanceof ServerPlayer) {
			MessageRegister.sendToPlayer(new MessageBlink(offX, offY, offZ), (ServerPlayer) e);
		}
	}

}
