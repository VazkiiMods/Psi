/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 19:06:33 (GMT)]
 */
package vazkii.psi.common.spell.selector.entity;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.entity.EntitySpellProjectile;

public class PieceSelectorNearbyProjectiles  extends PieceSelectorNearby {

	public PieceSelectorNearbyProjectiles(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate() {
		return (Entity e) -> { return e instanceof IProjectile && !(e instanceof EntitySpellProjectile); };
	}

}
