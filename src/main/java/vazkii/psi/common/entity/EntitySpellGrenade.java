/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

import java.util.Optional;

public class EntitySpellGrenade extends EntitySpellProjectile {
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_GRENADE)
	public static EntityType<EntitySpellGrenade> TYPE;

	boolean sound = false;

	public EntitySpellGrenade(EntityType<? extends ThrowableEntity> type, World worldIn) {
		super(type, worldIn);
	}

	protected EntitySpellGrenade(EntityType<? extends ThrowableEntity> type, World worldIn, LivingEntity throwerIn) {
		super(type, worldIn, throwerIn);

		double speed = 0.65;
		setMotion(getMotion().mul(speed, speed, speed));
	}

	public EntitySpellGrenade(World world, LivingEntity thrower) {
		this(TYPE, world, thrower);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	public void tick() {
		super.tick();

		if (ticksExisted > 60 && isAlive() && explodes()) {
			doExplosion();
		}
	}

	public void doExplosion() {
		if (getAttackTarget() != null) {
			cast((SpellContext context) -> {
				if (context != null) {
					context.attackedEntity = getAttackTarget();
				}
			});
		} else {
			cast();
		}
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
		double m = 0.1;
		for (int j = 0; j < 40; j++) {
			double d0 = getEntityWorld().rand.nextGaussian() * m;
			double d1 = getEntityWorld().rand.nextGaussian() * m;
			double d2 = getEntityWorld().rand.nextGaussian() * m;

			double x = getX() + 0.75 * getEntityWorld().rand.nextFloat() - 0.375;
			double y = getY() + 0.5 * getEntityWorld().rand.nextFloat();
			double z = getZ() + 0.75 * getEntityWorld().rand.nextFloat() - 0.375;
			getEntityWorld().addParticle(ParticleTypes.EXPLOSION, x, y, z, d0, d1, d2);
		}
	}

	public boolean explodes() {
		return true;
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult ray) {
		if (ray instanceof EntityRayTraceResult && ((EntityRayTraceResult) ray).getEntity() instanceof LivingEntity) {
			dataManager.set(ATTACKTARGET_UUID, Optional.of(((EntityRayTraceResult) ray).getEntity().getUniqueID()));
		}
		if (!getEntityWorld().isRemote && !sound && explodes()) {
			playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2F, 1F);
			sound = true;
		}

		if (ray.getType() == RayTraceResult.Type.BLOCK) {
			Direction face = ((BlockRayTraceResult) ray).getFace();
			Vector3 position = Vector3.fromVec3d(ray.getHitVec());
			if (face != Direction.UP) {
				position.add(Vector3.fromDirection(face).multiply(0.1d));
			}
			setPositionAndUpdate(position.x, position.y, position.z);
			setMotion(Vec3d.ZERO);
		}
		else if (ray.getType() == RayTraceResult.Type.ENTITY) {
			setPositionAndUpdate(ray.getHitVec().x, ray.getHitVec().y, ray.getHitVec().z);
			setMotion(Vec3d.ZERO);
		}
	}

}
