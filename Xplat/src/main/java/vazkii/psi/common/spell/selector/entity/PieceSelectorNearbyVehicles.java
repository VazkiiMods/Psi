/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyVehicles extends PieceSelectorNearby {

	public PieceSelectorNearbyVehicles(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof Minecart || e instanceof Boat || (e instanceof AbstractHorse && ((AbstractHorse) e).isSaddled()) || (e instanceof Pig && ((Pig) e).isSaddled());
	}
}
