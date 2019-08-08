/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/02/2016, 19:44:25 (GMT)]
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.potion.Effects;
import net.minecraft.potion.Effect;
import vazkii.psi.api.spell.Spell;

public class PieceTrickInvisibility extends PieceTrickPotionBase {

	public PieceTrickInvisibility(Spell spell) {
		super(spell);
	}

	@Override
	public Effect getPotion() {
		return Effects.INVISIBILITY;
	}

	@Override
	public boolean hasPower() {
		return false;
	}

}
