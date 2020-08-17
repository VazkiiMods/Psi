/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

public class EntitySpellCharge extends EntitySpellGrenade implements IDetonationHandler {
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_CHARGE)
	public static EntityType<EntitySpellCharge> TYPE;

	public EntitySpellCharge(EntityType<? extends ThrowableEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public EntitySpellCharge(World worldIn, LivingEntity throwerIn) {
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
	public Vector3d objectLocus() {
		return getPositionVec();
	}

	@Override
	public void detonate() {
		doExplosion();
	}
}
