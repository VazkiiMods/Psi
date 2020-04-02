/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [18/01/2016, 22:18:31 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickExplode extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Number> power;

	public PieceTrickExplode(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double powerVal = this.<Double>getParamEvaluation(power);
		if(powerVal == null || powerVal <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		
		powerVal = Math.max(1, powerVal);

		meta.addStat(EnumSpellStat.POTENCY, (int) (powerVal * 70));
		meta.addStat(EnumSpellStat.COST, (int) (powerVal * 210));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		double powerVal = this.getParamValue(context, power).doubleValue();

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		
		BlockPos pos = positionVal.toBlockPos();
		BlockState state = context.caster.getEntityWorld().getBlockState(pos);
		
		context.caster.getEntityWorld().createExplosion(context.focalPoint, positionVal.x, positionVal.y, positionVal.z, (float) powerVal, state.getMaterial().isLiquid() ? Explosion.Mode.NONE : Explosion.Mode.DESTROY);
		return null;
	}
	

}
