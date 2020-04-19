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
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellProjectile;

import java.util.function.Predicate;

public class PieceSelectorNearbyProjectiles extends PieceSelectorNearby {

	public PieceSelectorNearbyProjectiles(Spell spell) {
		super(spell);
	}

	@Override
	public Predicate<Entity> getTargetPredicate(SpellContext context) {
		return (Entity e) -> (e instanceof IProjectile || e instanceof DamagingProjectileEntity || e instanceof EyeOfEnderEntity) && !(e instanceof EntitySpellProjectile);
	}

}
