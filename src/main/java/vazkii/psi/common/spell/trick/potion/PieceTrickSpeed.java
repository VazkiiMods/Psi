/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/02/2016, 19:39:17 (GMT)]
 */
package vazkii.psi.common.spell.trick.potion;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import vazkii.psi.api.spell.Spell;

public class PieceTrickSpeed extends PieceTrickPotionBase {

	public PieceTrickSpeed(Spell spell) {
		super(spell);
	}

	@Override
	public Potion getPotion() {
		return MobEffects.SPEED;
	}

}
