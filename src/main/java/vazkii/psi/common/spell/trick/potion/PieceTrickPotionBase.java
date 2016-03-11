/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/02/2016, 19:24:01 (GMT)]
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
import vazkii.psi.common.spell.trick.PieceTrickBlaze;

public abstract class PieceTrickPotionBase extends PieceTrick {

	SpellParam target;
	SpellParam power;
	SpellParam time;

	public PieceTrickPotionBase(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		if(hasPower())
			addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, true));
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double powerVal = 1D;
		if(hasPower())
			powerVal = this.<Double>getParamEvaluation(power);
		Double timeVal = this.<Double>getParamEvaluation(time);

		if(powerVal == null || timeVal == null || powerVal <= 0 || powerVal.doubleValue() != powerVal.intValue() || timeVal <= 0 || timeVal.doubleValue() != timeVal.intValue())
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

		meta.addStat(EnumSpellStat.POTENCY, 20 + getPotency(powerVal.intValue(), timeVal.intValue()));
		meta.addStat(EnumSpellStat.COST, 40 + getCost(powerVal.intValue(), timeVal.intValue()));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);

		context.verifyEntity(targetVal);
		if(!(targetVal instanceof EntityLivingBase))
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		if(!context.isInRadius(targetVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		Double powerVal = null;
		if(hasPower())
			powerVal = this.<Double>getParamValue(context, power);
		Double timeVal = this.<Double>getParamValue(context, time);

		((EntityLivingBase) targetVal).addPotionEffect(new PotionEffect(getPotion().id, Math.max(1, timeVal.intValue()) * 20, hasPower() ? Math.max(0, powerVal.intValue() - 1) : 0));

		return null;
	}

	public abstract Potion getPotion();

	public int getCost(int power, int time) throws SpellCompilationException {
		return (int) multiplySafe(getPotency(power, time) * 5);
	}

	public int getPotency(int power, int time) throws SpellCompilationException {
		return (int) multiplySafe(time, power, power, 5);
	}

	public boolean hasPower() {
		return true;
	}

}
