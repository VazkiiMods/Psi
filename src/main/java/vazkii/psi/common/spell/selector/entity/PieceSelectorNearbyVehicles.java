/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.function.Predicate;

public class PieceSelectorNearbyVehicles extends PieceSelectorNearby {

	public PieceSelectorNearbyVehicles(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> e instanceof MinecartEntity || e instanceof BoatEntity || (e instanceof AbstractHorseEntity && ((AbstractHorseEntity) e).isSaddled()) || (e instanceof PigEntity && ((PigEntity) e).isSaddled());
	}
}
