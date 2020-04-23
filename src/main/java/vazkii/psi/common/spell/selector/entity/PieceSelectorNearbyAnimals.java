/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;

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
		return (Entity e) -> (e instanceof AnimalEntity || e instanceof WaterMobEntity) && !(e instanceof IMob);
	}

}
