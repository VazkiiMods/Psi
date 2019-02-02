/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 00:20:46 (GMT)]
 */
package vazkii.psi.common.spell.selector.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import vazkii.psi.api.spell.Spell;

public class PieceSelectorNearbyAnimals extends PieceSelectorNearby {

	public PieceSelectorNearbyAnimals(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate() {
		return (Entity e) -> e instanceof IAnimals && !(e instanceof IMob);
	}

}
