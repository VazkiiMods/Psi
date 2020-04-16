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
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
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

		if(ticksExisted > 60 && isAlive() && explodes())
			doExplosion();
	}

	public void doExplosion() {
		if(getAttackTarget() !=null) {
			cast((SpellContext context) -> {
				if (context != null) {
					context.attackedEntity = getAttackTarget();
				}
			});
		} else cast();
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1F);
		double m = 0.1;
		double d3 = 0.0D;
		for(int j = 0; j < 40; j++) {
            double d0 = getEntityWorld().rand.nextGaussian() * m;
            double d1 = getEntityWorld().rand.nextGaussian() * m;
            double d2 = getEntityWorld().rand.nextGaussian() * m;

            getEntityWorld().addParticle(ParticleTypes.EXPLOSION, getX() + getEntityWorld().rand.nextFloat() * getWidth() * 2.0F - getWidth() - d0 * d3, getY() + getEntityWorld().rand.nextFloat() * getHeight() - d1 * d3, getZ() + getEntityWorld().rand.nextFloat() * getWidth() * 2.0F - getWidth() - d2 * d3, d0, d1, d2);
        }
	}

	public boolean explodes() {
		return true;
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		if(pos instanceof EntityRayTraceResult && ((EntityRayTraceResult) pos).getEntity() instanceof LivingEntity) {
			dataManager.set(ATTACKTARGET_UUID, Optional.of(((EntityRayTraceResult) pos).getEntity().getUniqueID()));
		}
        if (!getEntityWorld().isRemote && !sound && explodes()) {
            playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2F, 1F);
            sound = true;
        }
        setPos(pos.getHitVec().x, pos.getHitVec().y, pos.getHitVec().z);
        setMotion(Vec3d.ZERO);
    }

}
