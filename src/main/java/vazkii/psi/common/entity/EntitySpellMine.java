/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

import java.util.List;
import java.util.Optional;

public class EntitySpellMine extends EntitySpellGrenade {
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_MINE)
	public static EntityType<EntitySpellMine> TYPE;

	boolean triggered = false;

	public EntitySpellMine(EntityType<? extends ThrowableEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public EntitySpellMine(World worldIn, LivingEntity throwerIn) {
		super(TYPE, worldIn, throwerIn);
	}

	@Override
	public void tick() {
		super.tick();

		List<LivingEntity> entities = getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1, 1, 1));
		Entity thrower = getOwner();
		if (thrower != null && tickCount < 30) {
			entities.remove(thrower);
		}

		if (!entities.isEmpty()) {
			if (!triggered) {
				playSound(SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, 0.5F, 0.6F);
			}
			triggered = true;
			entityData.set(ATTACKTARGET_UUID, Optional.of(entities.get(0).getUUID()));
		} else if (triggered) {
			doExplosion();
		}
	}

	@Override
	public int getParticleCount() {
		return 1;
	}

	@Override
	public int getLiveTime() {
		return 6000;
	}

	@Override
	public boolean explodes() {
		return false;
	}

}
