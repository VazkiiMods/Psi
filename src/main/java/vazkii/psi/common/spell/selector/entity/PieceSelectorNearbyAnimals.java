/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyAnimals extends PieceSelectorNearby {

	public PieceSelectorNearbyAnimals(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		// for which classes to check, see ServerWorld despawning when spawn-animals server property is false
		return (Entity e) -> (e instanceof Animal || e instanceof WaterAnimal) && !(e instanceof Enemy);
	}

}
