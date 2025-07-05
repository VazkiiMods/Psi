/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import vazkii.psi.api.spell.*;

public class PieceTrickWither extends PieceTrickPotionBase {

	public PieceTrickWither(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).mul(SpellParam.GENERIC_NAME_POWER, true).square().mul(20));
	}

	@Override
	public Holder<MobEffect> getPotion() {
		return MobEffects.WITHER;
	}

	@Override
	public int getPotency(int power, int time) throws SpellCompilationException {
		return super.getPotency(power, time) * 4;
	}

}
