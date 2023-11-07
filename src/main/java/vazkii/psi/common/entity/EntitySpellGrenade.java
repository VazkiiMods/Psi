/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

import java.util.Optional;

public class EntitySpellGrenade extends EntitySpellProjectile {
	@ObjectHolder(registryName = "minecraft:entity_type", value = LibResources.PREFIX_MOD + LibEntityNames.SPELL_GRENADE)
	public static EntityType<EntitySpellGrenade> TYPE;

	boolean sound = false;

	public EntitySpellGrenade(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
		super(type, worldIn);
	}

	protected EntitySpellGrenade(EntityType<? extends ThrowableProjectile> type, Level worldIn, LivingEntity throwerIn) {
		super(type, worldIn, throwerIn);

		double speed = 0.65;
		setDeltaMovement(getDeltaMovement().multiply(speed, speed, speed));
	}

	public EntitySpellGrenade(Level world, LivingEntity thrower) {
		this(TYPE, world, thrower);
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	public void tick() {
		super.tick();

		if(tickCount > 60 && isAlive() && explodes()) {
			doExplosion();
		}
	}

	public void doExplosion() {
		if(getAttackTarget() != null) {
			cast((SpellContext context) -> {
				if(context != null) {
					context.attackedEntity = getAttackTarget();
				}
			});
		} else {
			cast();
		}
		playSound(SoundEvents.GENERIC_EXPLODE, 0.5F, 1F);
		double m = 0.1;
		for(int j = 0; j < 40; j++) {
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
	protected void onHit(@Nonnull HitResult ray) {
		if(ray instanceof EntityHitResult && ((EntityHitResult) ray).getEntity() instanceof LivingEntity) {
			entityData.set(ATTACKTARGET_UUID, Optional.of(((EntityHitResult) ray).getEntity().getUUID()));
		}
		if(!getCommandSenderWorld().isClientSide && !sound && explodes()) {
			playSound(SoundEvents.CREEPER_PRIMED, 2F, 1F);
			sound = true;
		}

		if(ray.getType() == HitResult.Type.BLOCK) {
			Direction face = ((BlockHitResult) ray).getDirection();
			Vector3 position = Vector3.fromVec3d(ray.getLocation());
			if(face != Direction.UP) {
				position.add(Vector3.fromDirection(face).multiply(0.1d));
			}
			teleportTo(position.x, position.y, position.z);
			setDeltaMovement(Vec3.ZERO);
		} else if(ray.getType() == HitResult.Type.ENTITY) {
			teleportTo(ray.getLocation().x, ray.getLocation().y, ray.getLocation().z);
			setDeltaMovement(Vec3.ZERO);
		}
	}

}
