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
import net.minecraft.entity.LivingEntity;
import vazkii.psi.api.spell.Spell;

import java.util.function.Predicate;

public class PieceSelectorNearbyLiving extends PieceSelectorNearby {

	public PieceSelectorNearbyLiving(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate() {
		return (Entity e) -> e instanceof LivingEntity;
	}

}
