/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [18/01/2016, 22:32:11 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.entity.Entity;
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
		
		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(speedVal) * 5));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(speedVal) * 20));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);
		Vector3 directionVal = this.<Vector3>getParamValue(context, direction);
		Double speedVal = this.<Double>getParamValue(context, speed);

		if(targetVal == null)
			throw new SpellRuntimeException("nulltarget");
		if(directionVal == null)
			throw new SpellRuntimeException("nullvector");
		if(speedVal == null)
			speedVal = 1D;
		
		final double mul = 0.3;
		// TODO check against max values so you can't go hyperspeed
		directionVal.copy().normalize();
		targetVal.motionX += directionVal.x * speedVal * mul;
		targetVal.motionY += directionVal.y * speedVal * mul;
		targetVal.motionZ += directionVal.z * speedVal * mul;
		
		return null;
	}

}
