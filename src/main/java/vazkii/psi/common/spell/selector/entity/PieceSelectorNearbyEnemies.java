/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [23/01/2016, 00:20:46 (GMT)]
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.monster.IMob;
import vazkii.psi.api.spell.Spell;

public class PieceSelectorNearbyEnemies extends PieceSelectorNearby {

	public PieceSelectorNearbyEnemies(Spell spell) {
		super(spell);
	}

	@Override
	public Class getTargetClass() {
		return IMob.class;
	}

}
