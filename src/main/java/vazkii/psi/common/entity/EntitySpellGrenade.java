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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
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
		setDeltaMovement(getDeltaMovement().multiply(speed, speed, speed));
	}

	public EntitySpellGrenade(World world, LivingEntity thrower) {
		this(TYPE, world, thrower);
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	public void tick() {
		super.tick();

		if (tickCount > 60 && isAlive() && explodes()) {
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
		playSound(SoundEvents.GENERIC_EXPLODE, 0.5F, 1F);
		double m = 0.1;
		for (int j = 0; j < 40; j++) {
			double d0 = getCommandSenderWorld().random.nextGaussian() * m;
			double d1 = getCommandSenderWorld().random.nextGaussian() * m;
			double d2 = getCommandSenderWorld().random.nextGaussian() * m;

			double x = getX() + 0.75 * getCommandSenderWorld().random.nextFloat() - 0.375;
			double y = getY() + 0.5 * getCommandSenderWorld().random.nextFloat();
			double z = getZ() + 0.75 * getCommandSenderWorld().random.nextFloat() - 0.375;
			getCommandSenderWorld().addParticle(ParticleTypes.EXPLOSION, x, y, z, d0, d1, d2);
		}
	}

	public boolean explodes() {
		return true;
	}

	@Override
	protected void onHit(@Nonnull RayTraceResult ray) {
		if (ray instanceof EntityRayTraceResult && ((EntityRayTraceResult) ray).getEntity() instanceof LivingEntity) {
			entityData.set(ATTACKTARGET_UUID, Optional.of(((EntityRayTraceResult) ray).getEntity().getUUID()));
		}
		if (!getCommandSenderWorld().isClientSide && !sound && explodes()) {
			playSound(SoundEvents.CREEPER_PRIMED, 2F, 1F);
			sound = true;
		}

		if (ray.getType() == RayTraceResult.Type.BLOCK) {
			Direction face = ((BlockRayTraceResult) ray).getDirection();
			Vector3 position = Vector3.fromVec3d(ray.getLocation());
			if (face != Direction.UP) {
				position.add(Vector3.fromDirection(face).multiply(0.1d));
			}
			teleportTo(position.x, position.y, position.z);
			setDeltaMovement(Vector3d.ZERO);
		} else if (ray.getType() == RayTraceResult.Type.ENTITY) {
			teleportTo(ray.getLocation().x, ray.getLocation().y, ray.getLocation().z);
			setDeltaMovement(Vector3d.ZERO);
		}
	}

}
