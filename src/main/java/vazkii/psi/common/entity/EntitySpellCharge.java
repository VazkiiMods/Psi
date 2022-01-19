/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

public class EntitySpellCharge extends EntitySpellGrenade implements IDetonationHandler {
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_CHARGE)
	public static EntityType<EntitySpellCharge> TYPE;

	public EntitySpellCharge(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
		super(type, worldIn);
	}

	public EntitySpellCharge(Level worldIn, LivingEntity throwerIn) {
		super(TYPE, worldIn, throwerIn);
	}

	@Override
	public int getParticleCount() {
		return 2;
	}

	@Override
	public int getLiveTime() {
		return 6000;
	}

	@Override
	public boolean explodes() {
		return false;
	}

	@Override
	public Vec3 objectLocus() {
		return position();
	}

	@Override
	public void detonate() {
		doExplosion();
	}
}
