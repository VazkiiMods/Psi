/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public abstract class PieceTrickPotionBase extends PieceTrick {

	SpellParam<Entity> target;
	SpellParam<Number> power;
	SpellParam<Number> time;

	public PieceTrickPotionBase(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).mul(SpellParam.GENERIC_NAME_POWER, true).square().mul(5));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).mul(SpellParam.GENERIC_NAME_POWER, true).square().mul(5).square());
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		if (hasPower()) {
			addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, true));
		}
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double powerVal = 1D;
		if (hasPower()) {
			powerVal = this.<Double>getParamEvaluation(power);
		}
		Double timeVal = this.<Double>getParamEvaluation(time);

		if (powerVal == null || timeVal == null || powerVal <= 0 || powerVal != powerVal.intValue() || timeVal <= 0 || timeVal != timeVal.intValue()) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, 20 + getPotency(powerVal.intValue(), timeVal.intValue()));
		meta.addStat(EnumSpellStat.COST, 40 + getCost(powerVal.intValue(), timeVal.intValue()));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);

		context.verifyEntity(targetVal);
		if (!(targetVal instanceof LivingEntity)) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		if (!context.isInRadius(targetVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		double powerVal = 1.0;
		if (hasPower()) {
			powerVal = this.getParamValue(context, power).doubleValue();
		}
		double timeVal = this.getParamValue(context, time).doubleValue();

		((LivingEntity) targetVal).addEffect(new MobEffectInstance(getPotion(), Math.max(1, (int) timeVal) * 20, hasPower() ? Math.max(0, (int) powerVal - 1) : 0));

		return null;
	}

	public abstract MobEffect getPotion();

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
