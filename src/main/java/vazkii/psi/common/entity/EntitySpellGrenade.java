/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/02/2016, 16:15:38 (GMT)]
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class EntitySpellGrenade extends EntitySpellProjectile {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibEntityNames.SPELL_GRENADE)
	public static EntityType<EntitySpellGrenade> TYPE;

	boolean sound = false;

	public EntitySpellGrenade(EntityType<?> type, World worldIn) {
		super(type, worldIn);
	}

	protected EntitySpellGrenade(EntityType<?> type, World worldIn, LivingEntity throwerIn) {
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

		if(ticksExisted > 60 && isAlive() && explodes())
			doExplosion();
	}

	public void doExplosion() {
		cast();
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
		double m = 0.1;
		double d3 = 0.0D;
		for(int j = 0; j < 40; j++) {
			double d0 = getEntityWorld().rand.nextGaussian() * m;
			double d1 = getEntityWorld().rand.nextGaussian() * m;
			double d2 = getEntityWorld().rand.nextGaussian() * m;

			getEntityWorld().addParticle(ParticleTypes.EXPLOSION, posX + getEntityWorld().rand.nextFloat() * getWidth() * 2.0F - getWidth() - d0 * d3, posY + getEntityWorld().rand.nextFloat() * getHeight() - d1 * d3, posZ + getEntityWorld().rand.nextFloat() * getWidth() * 2.0F - getWidth() - d2 * d3, d0, d1, d2);
		}
	}

	public boolean explodes() {
		return true;
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		if(!getEntityWorld().isRemote && !sound && explodes()) {
			playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2F, 1F);
			sound = true;
		}

		posX = pos.getHitVec().x;
		posY = pos.getHitVec().y;
		posZ = pos.getHitVec().z;
		setMotion(Vec3d.ZERO);
	}

}
