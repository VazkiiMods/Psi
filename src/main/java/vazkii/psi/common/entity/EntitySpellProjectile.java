/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class EntitySpellProjectile extends ThrowableProjectile {
	@ObjectHolder(registryName = "minecraft:entity_type", value = LibResources.PREFIX_MOD + LibEntityNames.SPELL_PROJECTILE)
	public static EntityType<EntitySpellProjectile> TYPE;

	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	private static final String TAG_TIME_ALIVE = "timeAlive";

	private static final String TAG_LAST_MOTION_X = "lastMotionX";
	private static final String TAG_LAST_MOTION_Y = "lastMotionY";
	private static final String TAG_LAST_MOTION_Z = "lastMotionZ";

	private static final EntityDataAccessor<ItemStack> COLORIZER_DATA = SynchedEntityData.defineId(EntitySpellProjectile.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<ItemStack> BULLET_DATA = SynchedEntityData.defineId(EntitySpellProjectile.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Optional<UUID>> CASTER_UUID = SynchedEntityData.defineId(EntitySpellProjectile.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Optional<UUID>> ATTACKTARGET_UUID = SynchedEntityData.defineId(EntitySpellProjectile.class, EntityDataSerializers.OPTIONAL_UUID);

	public SpellContext context;
	public int timeAlive;

	public EntitySpellProjectile(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
		super(type, worldIn);
	}

	protected EntitySpellProjectile(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity thrower) {
		super(type, thrower, world);

		setOwner(thrower);
		setRot(thrower.getYRot() + 180, -thrower.getXRot());
		float f = 1.5F;
		double mx = Mth.sin(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(Mth.cos(getYRot() / 180.0F * (float) Math.PI) * Mth.cos(getXRot() / 180.0F * (float) Math.PI) * f) / 2D;
		double my = Mth.sin(getXRot() / 180.0F * (float) Math.PI) * f / 2D;
		this.push(mx, my, mz);
	}

	public EntitySpellProjectile(Level world, LivingEntity thrower) {
		this(TYPE, world, thrower);
	}

	public EntitySpellProjectile setInfo(Player player, ItemStack colorizer, ItemStack bullet) {
		entityData.set(COLORIZER_DATA, colorizer);
		entityData.set(BULLET_DATA, bullet.copy());
		entityData.set(CASTER_UUID, Optional.of(player.getUUID()));
		entityData.set(ATTACKTARGET_UUID, Optional.empty());
		return this;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(COLORIZER_DATA, ItemStack.EMPTY);
		entityData.define(BULLET_DATA, ItemStack.EMPTY);
		entityData.define(CASTER_UUID, Optional.empty());
		entityData.define(ATTACKTARGET_UUID, Optional.empty());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tagCompound) {
		super.addAdditionalSaveData(tagCompound);

		CompoundTag colorizerCmp = new CompoundTag();
		ItemStack colorizer = entityData.get(COLORIZER_DATA);
		if(!colorizer.isEmpty()) {
			colorizerCmp = colorizer.save(colorizerCmp);
		}
		tagCompound.put(TAG_COLORIZER, colorizerCmp);

		CompoundTag bulletCmp = new CompoundTag();
		ItemStack bullet = entityData.get(BULLET_DATA);
		if(!bullet.isEmpty()) {
			bulletCmp = bullet.save(bulletCmp);
		}
		tagCompound.put(TAG_BULLET, bulletCmp);

		tagCompound.putInt(TAG_TIME_ALIVE, timeAlive);

		tagCompound.putDouble(TAG_LAST_MOTION_X, getDeltaMovement().x());
		tagCompound.putDouble(TAG_LAST_MOTION_Y, getDeltaMovement().y());
		tagCompound.putDouble(TAG_LAST_MOTION_Z, getDeltaMovement().z());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tagCompound) {
		super.readAdditionalSaveData(tagCompound);

		CompoundTag colorizerCmp = tagCompound.getCompound(TAG_COLORIZER);
		ItemStack colorizer = ItemStack.of(colorizerCmp);
		entityData.set(COLORIZER_DATA, colorizer);

		CompoundTag bulletCmp = tagCompound.getCompound(TAG_BULLET);
		ItemStack bullet = ItemStack.of(bulletCmp);
		entityData.set(BULLET_DATA, bullet);

		Entity thrower = getOwner();
		if(thrower instanceof Player) {
			entityData.set(CASTER_UUID, Optional.of(thrower.getUUID()));
		}

		timeAlive = tagCompound.getInt(TAG_TIME_ALIVE);

		double lastMotionX = tagCompound.getDouble(TAG_LAST_MOTION_X);
		double lastMotionY = tagCompound.getDouble(TAG_LAST_MOTION_Y);
		double lastMotionZ = tagCompound.getDouble(TAG_LAST_MOTION_Z);
		setDeltaMovement(lastMotionX, lastMotionY, lastMotionZ);
	}

	@Override
	public void tick() {
		super.tick();

		int timeAlive = tickCount;
		if(timeAlive > getLiveTime()) {
			remove(RemovalReason.DISCARDED);
		}

		ItemStack colorizer = entityData.get(COLORIZER_DATA);
		int colorVal = Psi.proxy.getColorForColorizer(colorizer);

		float r = PsiRenderHelper.r(colorVal) / 255F;
		float g = PsiRenderHelper.g(colorVal) / 255F;
		float b = PsiRenderHelper.b(colorVal) / 255F;

		double x = getX();
		double y = getY();
		double z = getZ();

		Vector3 lookOrig = new Vector3(getDeltaMovement()).normalize();
		for(int i = 0; i < getParticleCount(); i++) {
			Vector3 look = lookOrig.copy();
			double spread = 0.6;
			double dist = 0.15;
			if(this instanceof EntitySpellGrenade) {
				look.y += 1;
				dist = 0.05;
			}

			look.x += (Math.random() - 0.5) * spread;
			look.y += (Math.random() - 0.5) * spread;
			look.z += (Math.random() - 0.5) * spread;

			look.normalize().multiply(dist);

			if(level().isClientSide()) {
				Psi.proxy.sparkleFX(level(), x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 1.2F, 12);
			}

		}
	}

	public int getLiveTime() {
		return 600;
	}

	public int getParticleCount() {
		return 5;
	}

	@Override
	protected void onHit(@Nonnull HitResult pos) {
		if(pos instanceof EntityHitResult && ((EntityHitResult) pos).getEntity() instanceof LivingEntity) {
			cast((SpellContext context) -> {
				if(context != null) {
					context.attackedEntity = (LivingEntity) ((EntityHitResult) pos).getEntity();
				}
			});
		} else {
			cast();
		}
	}

	public void cast() {
		cast(null);
	}

	public void cast(Consumer<SpellContext> callback) {
		Entity thrower = getOwner();
		boolean canCast = false;

		if(thrower instanceof Player) {
			ItemStack spellContainer = entityData.get(BULLET_DATA);
			if(!spellContainer.isEmpty() && ISpellAcceptor.isContainer(spellContainer)) {
				Spell spell = ISpellAcceptor.acceptor(spellContainer).getSpell();
				if(spell != null) {
					canCast = true;
					if(context == null) {
						context = new SpellContext().setPlayer((Player) thrower).setFocalPoint(this).setSpell(spell);
					}
					context.setFocalPoint(this);
				}
			}
		}

		if(callback != null) {
			callback.accept(context);
		}

		if(canCast && context != null) {
			context.cspell.safeExecute(context);
		}

		remove(RemovalReason.DISCARDED);
	}

	@Override
	public Entity getOwner() {
		Entity superThrower = super.getOwner();
		if(superThrower != null) {
			return superThrower;
		}

		return entityData.get(CASTER_UUID)
				.map(u -> getCommandSenderWorld().getPlayerByUUID(u))
				.orElse(null);
	}

	public LivingEntity getAttackTarget() {
		double radiusVal = SpellContext.MAX_DISTANCE;
		Vector3 positionVal = Vector3.fromVec3d(this.position());
		AABB axis = new AABB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);
		return entityData.get(ATTACKTARGET_UUID)
				.map(u -> {
					List<LivingEntity> a = getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, axis, (Entity e) -> e.getUUID().equals(u));
					if(!a.isEmpty()) {
						return a.get(0);
					}
					return null;
				})
				.orElse(null);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Nonnull
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
