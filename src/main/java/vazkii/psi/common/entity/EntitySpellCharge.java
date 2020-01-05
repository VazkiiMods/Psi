/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 18:05:24 (GMT)]
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.spell.detonator.IDetonationHandler;

public class EntitySpellCharge extends EntitySpellGrenade implements IDetonationHandler {

	public EntitySpellCharge(EntityType<?> type, World worldIn) {
		super(type, worldIn);
	}

	public EntitySpellCharge(World worldIn, LivingEntity throwerIn) {
		super(worldIn, throwerIn);
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
	public Vec3d objectLocus() {
		return getPositionVector();
	}

	@Override
	public void detonate() {
		doExplosion();
	}
}
