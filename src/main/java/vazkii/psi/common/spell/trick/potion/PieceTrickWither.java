/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;

public class PieceTrickWither extends PieceTrickPotionBase {

	public PieceTrickWither(Spell spell) {
		super(spell);
	}

	@Override
	public Effect getPotion() {
		return Effects.WITHER;
	}

	@Override
	public int getPotency(int power, int time) throws SpellCompilationException {
		return super.getPotency(power, time) * 4;
	}

}
