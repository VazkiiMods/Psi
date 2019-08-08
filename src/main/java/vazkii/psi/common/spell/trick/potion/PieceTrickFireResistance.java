/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/02/2016, 19:43:34 (GMT)]
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.potion.Effects;
import net.minecraft.potion.Effect;
import vazkii.psi.api.spell.Spell;

public class PieceTrickFireResistance extends PieceTrickPotionBase {

	public PieceTrickFireResistance(Spell spell) {
		super(spell);
	}

	@Override
	public Effect getPotion() {
		return Effects.FIRE_RESISTANCE;
	}

	@Override
	public boolean hasPower() {
		return false;
	}

}
