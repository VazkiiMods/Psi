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

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import vazkii.psi.api.spell.Spell;

import java.util.function.Predicate;

public class PieceSelectorNearbyAnimals extends PieceSelectorNearby {

	public PieceSelectorNearbyAnimals(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate() {
		// for which classes to check, see ServerWorld despawning when spawn-animals server property is false
		return (Entity e) -> (e instanceof AnimalEntity || e instanceof WaterMobEntity) && !(e instanceof IMob);
	}

}
