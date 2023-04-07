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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibEntityNames;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;
import java.util.UUID;

public class EntitySpellCircle extends Entity implements ISpellImmune {
	@ObjectHolder(registryName = "minecraft:entity_type", value = LibResources.PREFIX_MOD + LibEntityNames.SPELL_CIRCLE)
	public static EntityType<EntitySpellCircle> TYPE;

	public static final int CAST_TIMES = 20;
	public static final int CAST_DELAY = 5;
	public static final int LIVE_TIME = (CAST_TIMES + 2) * CAST_DELAY;

	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	private static final String TAG_CASTER = "caster";
	private static final String TAG_TIME_ALIVE = "timeAlive";
	private static final String TAG_TIMES_CAST = "timesCast";

	private static final String TAG_LOOK_X = "savedLookX";
	private static final String TAG_LOOK_Y = "savedLookY";
	private static final String TAG_LOOK_Z = "savedLookZ";

	public static final EntityDataAccessor<ItemStack> COLORIZER_DATA = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<ItemStack> BULLET_DATA = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Optional<UUID>> CASTER_UUID = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> TIME_ALIVE = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TIMES_CAST = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.INT);

	private static final EntityDataAccessor<Float> LOOK_X = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> LOOK_Y = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> LOOK_Z = SynchedEntityData.defineId(EntitySpellCircle.class, EntityDataSerializers.FLOAT);

	public EntitySpellCircle(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public EntitySpellCircle setInfo(Player player, ItemStack colorizer, ItemStack bullet) {
		entityData.set(COLORIZER_DATA, colorizer);
		entityData.set(BULLET_DATA, bullet.copy());
		entityData.set(CASTER_UUID, Optional.of(player.getUUID()));

		Vec3 lookVec = player.getViewVector(1F);
		entityData.set(LOOK_X, (float) lookVec.x);
		entityData.set(LOOK_Y, (float) lookVec.y);
		entityData.set(LOOK_Z, (float) lookVec.z);
		return this;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(COLORIZER_DATA, ItemStack.EMPTY);
		entityData.define(BULLET_DATA, ItemStack.EMPTY);
		entityData.define(CASTER_UUID, Optional.empty());
		entityData.define(TIME_ALIVE, 0);
		entityData.define(TIMES_CAST, 0);
		entityData.define(LOOK_X, 0F);
		entityData.define(LOOK_Y, 0F);
		entityData.define(LOOK_Z, 0F);
	}

	@Override
	public void addAdditionalSaveData(@Nonnull CompoundTag tagCompound) {
		CompoundTag colorizerCmp = new CompoundTag();
		ItemStack colorizer = entityData.get(COLORIZER_DATA);
		if (!colorizer.isEmpty()) {
			colorizerCmp = colorizer.save(colorizerCmp);
		}
		tagCompound.put(TAG_COLORIZER, colorizerCmp);

		CompoundTag bulletCmp = new CompoundTag();
		ItemStack bullet = entityData.get(BULLET_DATA);
		if (!bullet.isEmpty()) {
			bulletCmp = bullet.save(bulletCmp);
		}
		tagCompound.put(TAG_BULLET, bulletCmp);

		entityData.get(CASTER_UUID).ifPresent(u -> tagCompound.putString(TAG_CASTER, u.toString()));
		tagCompound.putInt(TAG_TIME_ALIVE, getTimeAlive());
		tagCompound.putInt(TAG_TIMES_CAST, entityData.get(TIMES_CAST));

		tagCompound.putFloat(TAG_LOOK_X, entityData.get(LOOK_X));
		tagCompound.putFloat(TAG_LOOK_Y, entityData.get(LOOK_Y));
		tagCompound.putFloat(TAG_LOOK_Z, entityData.get(LOOK_Z));
	}

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundTag tagCompound) {
		CompoundTag colorizerCmp = tagCompound.getCompound(TAG_COLORIZER);
		ItemStack colorizer = ItemStack.of(colorizerCmp);
		entityData.set(COLORIZER_DATA, colorizer);

		CompoundTag bulletCmp = tagCompound.getCompound(TAG_BULLET);
		ItemStack bullet = ItemStack.of(bulletCmp);
		entityData.set(BULLET_DATA, bullet);

		if (tagCompound.contains(TAG_CASTER)) {
			entityData.set(CASTER_UUID, Optional.of(UUID.fromString(tagCompound.getString(TAG_CASTER))));
		}
		setTimeAlive(tagCompound.getInt(TAG_TIME_ALIVE));
		entityData.set(TIMES_CAST, tagCompound.getInt(TAG_TIMES_CAST));

		entityData.set(LOOK_X, tagCompound.getFloat(TAG_LOOK_X));
		entityData.set(LOOK_Y, tagCompound.getFloat(TAG_LOOK_Y));
		entityData.set(LOOK_Z, tagCompound.getFloat(TAG_LOOK_Z));
	}

	@Override
	public void tick() {
		super.tick();

		int timeAlive = getTimeAlive();
		if (timeAlive > LIVE_TIME) {
			remove(RemovalReason.DISCARDED);
		}

		setTimeAlive(timeAlive + 1);
		int times = entityData.get(TIMES_CAST);

		if (timeAlive > CAST_DELAY && timeAlive % CAST_DELAY == 0 && times < 20) {
			SpellContext context = null;
			Entity thrower = getCaster();
			if (thrower instanceof Player) {
				ItemStack spellContainer = entityData.get(BULLET_DATA);
				if (!spellContainer.isEmpty() && ISpellAcceptor.isContainer(spellContainer)) {
					entityData.set(TIMES_CAST, times + 1);
					Spell spell = ISpellAcceptor.acceptor(spellContainer).getSpell();
					if (spell != null) {
						context = new SpellContext().setPlayer((Player) thrower).setFocalPoint(this)
								.setSpell(spell).setLoopcastIndex(times);
					}
				}
			}

			if (context != null) {
				context.cspell.safeExecute(context);
			}
		}

		if (level.isClientSide) {
			ItemStack colorizer = entityData.get(COLORIZER_DATA);
			int colorVal = Psi.proxy.getColorForColorizer(colorizer);

			float r = PsiRenderHelper.r(colorVal) / 255F;
			float g = PsiRenderHelper.g(colorVal) / 255F;
			float b = PsiRenderHelper.b(colorVal) / 255F;
			for (int i = 0; i < 5; i++) {
				double x = getX() + (Math.random() - 0.5) * getBbWidth();
				double y = getY() - getMyRidingOffset();
				double z = getZ() + (Math.random() - 0.5) * getBbWidth();
				float grav = -0.15F - (float) Math.random() * 0.03F;
				Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
			}
		}

	}

	@Override
	public Vec3 getLookAngle() {
		float x = entityData.get(LOOK_X);
		float y = entityData.get(LOOK_Y);
		float z = entityData.get(LOOK_Z);
		return new Vec3(x, y, z);
	}

	public int getTimeAlive() {
		return entityData.get(TIME_ALIVE);
	}

	public void setTimeAlive(int i) {
		entityData.set(TIME_ALIVE, i);
	}

	@Nullable
	public LivingEntity getCaster() {
		return entityData.get(CASTER_UUID).map(getCommandSenderWorld()::getPlayerByUUID).orElse(null);
	}

	@Override
	public boolean isImmune() {
		return true;
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
