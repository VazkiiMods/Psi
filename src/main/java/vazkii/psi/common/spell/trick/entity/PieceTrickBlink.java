/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [29/01/2016, 17:04:45 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import javax.swing.LookAndFeel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
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

public class PieceTrickBlink extends PieceTrick {

	SpellParam target;
	SpellParam distance;
	
	public PieceTrickBlink(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(distance = new ParamNumber("psi.spellparam.distance", SpellParam.RED, false, true));
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double distanceVal = this.<Double>getParamEvaluation(distance);
		if(distanceVal == null)
			distanceVal = 1D;
		
		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 30));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 40));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);
		Double distanceVal = this.<Double>getParamValue(context, distance);

		blink(context, targetVal, distanceVal);
		
		return null;
	}
	
	public static void blink(SpellContext context, Entity e, double dist) throws SpellRuntimeException {
		if(e == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		if(!context.isInRadius(e))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		
		Vec3 look = e.getLookVec();
		if(look == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		double x = e.posX += look.xCoord * dist;
		double y = e.posY += look.yCoord * dist;
		double z = e.posZ += look.zCoord * dist;

		if(e instanceof EntityPlayerMP) {
			EntityPlayerMP mp = (EntityPlayerMP) e;
			mp.playerNetServerHandler.setPlayerLocation(x, y, z, e.rotationYaw, e.rotationPitch);
		} else e.setPosition(x, y, z);
	}

}
