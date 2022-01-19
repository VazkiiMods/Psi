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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
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

public class EntitySpellProjectile extends ThrowableEntity {
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_PROJECTILE)
	public static EntityType<EntitySpellProjectile> TYPE;

	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	private static final String TAG_TIME_ALIVE = "timeAlive";

	private static final String TAG_LAST_MOTION_X = "lastMotionX";
	private static final String TAG_LAST_MOTION_Y = "lastMotionY";
	private static final String TAG_LAST_MOTION_Z = "lastMotionZ";

	private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.defineId(EntitySpellProjectile.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<ItemStack> BULLET_DATA = EntityDataManager.defineId(EntitySpellProjectile.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Optional<UUID>> CASTER_UUID = EntityDataManager.defineId(EntitySpellProjectile.class, DataSerializers.OPTIONAL_UUID);
	protected static final DataParameter<Optional<UUID>> ATTACKTARGET_UUID = EntityDataManager.defineId(EntitySpellProjectile.class, DataSerializers.OPTIONAL_UUID);

	public SpellContext context;
	public int timeAlive;

	public EntitySpellProjectile(EntityType<? extends ThrowableEntity> type, World worldIn) {
		super(type, worldIn);
	}

	protected EntitySpellProjectile(EntityType<? extends ThrowableEntity> type, World world, LivingEntity thrower) {
		super(type, thrower, world);

		setOwner(thrower);
		setRot(thrower.yRot + 180, -thrower.xRot);
		float f = 1.5F;
		double mx = MathHelper.sin(yRot / 180.0F * (float) Math.PI) * MathHelper.cos(xRot / 180.0F * (float) Math.PI) * f / 2D;
		double mz = -(MathHelper.cos(yRot / 180.0F * (float) Math.PI) * MathHelper.cos(xRot / 180.0F * (float) Math.PI) * f) / 2D;
		double my = MathHelper.sin(xRot / 180.0F * (float) Math.PI) * f / 2D;
		setDeltaMovement(mx, my, mz);
	}

	public EntitySpellProjectile(World world, LivingEntity thrower) {
		this(TYPE, world, thrower);
	}

	public EntitySpellProjectile setInfo(PlayerEntity player, ItemStack colorizer, ItemStack bullet) {
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
	public void addAdditionalSaveData(CompoundNBT tagCompound) {
		super.addAdditionalSaveData(tagCompound);

		CompoundNBT colorizerCmp = new CompoundNBT();
		ItemStack colorizer = entityData.get(COLORIZER_DATA);
		if (!colorizer.isEmpty()) {
			colorizerCmp = colorizer.save(colorizerCmp);
		}
		tagCompound.put(TAG_COLORIZER, colorizerCmp);

		CompoundNBT bulletCmp = new CompoundNBT();
		ItemStack bullet = entityData.get(BULLET_DATA);
		if (!bullet.isEmpty()) {
			bulletCmp = bullet.save(bulletCmp);
		}
		tagCompound.put(TAG_BULLET, bulletCmp);

		tagCompound.putInt(TAG_TIME_ALIVE, timeAlive);

		tagCompound.putDouble(TAG_LAST_MOTION_X, getDeltaMovement().x());
		tagCompound.putDouble(TAG_LAST_MOTION_Y, getDeltaMovement().y());
		tagCompound.putDouble(TAG_LAST_MOTION_Z, getDeltaMovement().z());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT tagCompound) {
		super.readAdditionalSaveData(tagCompound);

		CompoundNBT colorizerCmp = tagCompound.getCompound(TAG_COLORIZER);
		ItemStack colorizer = ItemStack.of(colorizerCmp);
		entityData.set(COLORIZER_DATA, colorizer);

		CompoundNBT bulletCmp = tagCompound.getCompound(TAG_BULLET);
		ItemStack bullet = ItemStack.of(bulletCmp);
		entityData.set(BULLET_DATA, bullet);

		Entity thrower = getOwner();
		if (thrower instanceof PlayerEntity) {
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
		if (timeAlive > getLiveTime()) {
			remove();
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
		for (int i = 0; i < getParticleCount(); i++) {
			Vector3 look = lookOrig.copy();
			double spread = 0.6;
			double dist = 0.15;
			if (this instanceof EntitySpellGrenade) {
				look.y += 1;
				dist = 0.05;
			}

			look.x += (Math.random() - 0.5) * spread;
			look.y += (Math.random() - 0.5) * spread;
			look.z += (Math.random() - 0.5) * spread;

			look.normalize().multiply(dist);

			if (level.isClientSide()) {
				Psi.proxy.sparkleFX(x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 1.2F, 12);
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
	protected void onHit(@Nonnull RayTraceResult pos) {
		if (pos instanceof EntityRayTraceResult && ((EntityRayTraceResult) pos).getEntity() instanceof LivingEntity) {
			cast((SpellContext context) -> {
				if (context != null) {
					context.attackedEntity = (LivingEntity) ((EntityRayTraceResult) pos).getEntity();
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

		if (thrower instanceof PlayerEntity) {
			ItemStack spellContainer = entityData.get(BULLET_DATA);
			if (!spellContainer.isEmpty() && ISpellAcceptor.isContainer(spellContainer)) {
				Spell spell = ISpellAcceptor.acceptor(spellContainer).getSpell();
				if (spell != null) {
					canCast = true;
					if (context == null) {
						context = new SpellContext().setPlayer((PlayerEntity) thrower).setFocalPoint(this).setSpell(spell);
					}
					context.setFocalPoint(this);
				}
			}
		}

		if (callback != null) {
			callback.accept(context);
		}

		if (canCast && context != null) {
			context.cspell.safeExecute(context);
		}

		remove();
	}

	@Override
	public Entity getOwner() {
		Entity superThrower = super.getOwner();
		if (superThrower != null) {
			return superThrower;
		}

		return entityData.get(CASTER_UUID)
				.map(u -> getCommandSenderWorld().getPlayerByUUID(u))
				.orElse(null);
	}

	public LivingEntity getAttackTarget() {
		double radiusVal = SpellContext.MAX_DISTANCE;
		Vector3 positionVal = Vector3.fromVec3d(this.position());
		AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);
		return entityData.get(ATTACKTARGET_UUID)
				.map(u -> {
					List<LivingEntity> a = getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, axis, (Entity e) -> e.getUUID().equals(u));
					if (a.size() > 0) {
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
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
