/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [18/01/2016, 22:32:11 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
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

public class PieceTrickAddMotion extends PieceTrick {

	SpellParam target;
	SpellParam direction;
	SpellParam speed;

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
		if(speedVal == null)
			speedVal = 1D;

		double absSpeed = Math.abs(speedVal);
		double speedPo2 = absSpeed * absSpeed;

		meta.addStat(EnumSpellStat.POTENCY, (int) (speedPo2 * 3.5));
		meta.addStat(EnumSpellStat.COST, (int) (speedPo2 * 8));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);
		Vector3 directionVal = this.<Vector3>getParamValue(context, direction);
		Double speedVal = this.<Double>getParamValue(context, speed);

		addMotion(context, targetVal, directionVal, speedVal);

		return null;
	}

	public static void addMotion(SpellContext context, Entity e, Vector3 dir, double speed) throws SpellRuntimeException {
		if(!context.isInRadius(e))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		double mul = 0.3;
		if(e instanceof EntityThrowable)
			mul *= 10;

		dir = dir.copy().normalize().multiply(mul * speed);
		
		e.motionX += dir.x;
		if(!isInBounds(e.motionX, dir.x))
			e.motionX = dir.x;
		
		e.motionY += dir.y;
		if(!isInBounds(e.motionY, dir.y))
			e.motionY = dir.y;
		
		e.motionZ += dir.z;
		if(!isInBounds(e.motionZ, dir.z))
			e.motionZ = dir.z;
		
		if(e.motionY >= 0)
			e.fallDistance = 0;
	}
	
	private static boolean isInBounds(double d, double bound) {
		return bound < 0 ? -Math.abs(d) > bound : Math.abs(d) < bound;
	}

}
