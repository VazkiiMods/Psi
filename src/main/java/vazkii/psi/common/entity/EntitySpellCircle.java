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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
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
	@ObjectHolder(LibResources.PREFIX_MOD + LibEntityNames.SPELL_CIRCLE)
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

	public static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.ITEMSTACK);
	private static final DataParameter<ItemStack> BULLET_DATA = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.ITEMSTACK);
	private static final DataParameter<Optional<UUID>> CASTER_UUID = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Integer> TIME_ALIVE = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIMES_CAST = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.VARINT);

	private static final DataParameter<Float> LOOK_X = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> LOOK_Y = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> LOOK_Z = EntityDataManager.createKey(EntitySpellCircle.class, DataSerializers.FLOAT);

	public EntitySpellCircle(EntityType<?> type, World worldIn) {
		super(type, worldIn);
	}

	public EntitySpellCircle setInfo(PlayerEntity player, ItemStack colorizer, ItemStack bullet) {
		dataManager.set(COLORIZER_DATA, colorizer);
		dataManager.set(BULLET_DATA, bullet);
		dataManager.set(CASTER_UUID, Optional.of(player.getUniqueID()));

		Vector3d lookVec = player.getLook(1F);
		dataManager.set(LOOK_X, (float) lookVec.x);
		dataManager.set(LOOK_Y, (float) lookVec.y);
		dataManager.set(LOOK_Z, (float) lookVec.z);
		return this;
	}

	@Override
	protected void registerData() {
		dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
		dataManager.register(BULLET_DATA, ItemStack.EMPTY);
		dataManager.register(CASTER_UUID, Optional.empty());
		dataManager.register(TIME_ALIVE, 0);
		dataManager.register(TIMES_CAST, 0);
		dataManager.register(LOOK_X, 0F);
		dataManager.register(LOOK_Y, 0F);
		dataManager.register(LOOK_Z, 0F);
	}

	@Override
	public void writeAdditional(@Nonnull CompoundNBT tagCompound) {
		CompoundNBT colorizerCmp = new CompoundNBT();
		ItemStack colorizer = dataManager.get(COLORIZER_DATA);
		if (!colorizer.isEmpty()) {
			colorizerCmp = colorizer.write(colorizerCmp);
		}
		tagCompound.put(TAG_COLORIZER, colorizerCmp);

		CompoundNBT bulletCmp = new CompoundNBT();
		ItemStack bullet = dataManager.get(BULLET_DATA);
		if (!bullet.isEmpty()) {
			bulletCmp = bullet.write(bulletCmp);
		}
		tagCompound.put(TAG_BULLET, bulletCmp);

		dataManager.get(CASTER_UUID).ifPresent(u -> tagCompound.putString(TAG_CASTER, u.toString()));
		tagCompound.putInt(TAG_TIME_ALIVE, getTimeAlive());
		tagCompound.putInt(TAG_TIMES_CAST, dataManager.get(TIMES_CAST));

		tagCompound.putFloat(TAG_LOOK_X, dataManager.get(LOOK_X));
		tagCompound.putFloat(TAG_LOOK_Y, dataManager.get(LOOK_Y));
		tagCompound.putFloat(TAG_LOOK_Z, dataManager.get(LOOK_Z));
	}

	@Override
	public void readAdditional(@Nonnull CompoundNBT tagCompound) {
		CompoundNBT colorizerCmp = tagCompound.getCompound(TAG_COLORIZER);
		ItemStack colorizer = ItemStack.read(colorizerCmp);
		dataManager.set(COLORIZER_DATA, colorizer);

		CompoundNBT bulletCmp = tagCompound.getCompound(TAG_BULLET);
		ItemStack bullet = ItemStack.read(bulletCmp);
		dataManager.set(BULLET_DATA, bullet);

		if (tagCompound.contains(TAG_CASTER)) {
			dataManager.set(CASTER_UUID, Optional.of(UUID.fromString(tagCompound.getString(TAG_CASTER))));
		}
		setTimeAlive(tagCompound.getInt(TAG_TIME_ALIVE));
		dataManager.set(TIMES_CAST, tagCompound.getInt(TAG_TIMES_CAST));

		dataManager.set(LOOK_X, tagCompound.getFloat(TAG_LOOK_X));
		dataManager.set(LOOK_Y, tagCompound.getFloat(TAG_LOOK_Y));
		dataManager.set(LOOK_Z, tagCompound.getFloat(TAG_LOOK_Z));
	}

	@Override
	public void tick() {
		super.tick();

		int timeAlive = getTimeAlive();
		if (timeAlive > LIVE_TIME) {
			remove();
		}

		setTimeAlive(timeAlive + 1);
		int times = dataManager.get(TIMES_CAST);

		if (timeAlive > CAST_DELAY && timeAlive % CAST_DELAY == 0 && times < 20) {
			SpellContext context = null;
			Entity thrower = getCaster();
			if (thrower instanceof PlayerEntity) {
				ItemStack spellContainer = dataManager.get(BULLET_DATA);
				if (!spellContainer.isEmpty() && ISpellAcceptor.isContainer(spellContainer)) {
					dataManager.set(TIMES_CAST, times + 1);
					Spell spell = ISpellAcceptor.acceptor(spellContainer).getSpell();
					if (spell != null) {
						context = new SpellContext().setPlayer((PlayerEntity) thrower).setFocalPoint(this)
								.setSpell(spell).setLoopcastIndex(times);
					}
				}
			}

			if (context != null) {
				context.cspell.safeExecute(context);
			}
		}

		if (world.isRemote) {
			ItemStack colorizer = dataManager.get(COLORIZER_DATA);
			int colorVal = Psi.proxy.getColorForColorizer(colorizer);

			float r = PsiRenderHelper.r(colorVal) / 255F;
			float g = PsiRenderHelper.g(colorVal) / 255F;
			float b = PsiRenderHelper.b(colorVal) / 255F;
			for (int i = 0; i < 5; i++) {
				double x = getX() + (Math.random() - 0.5) * getWidth();
				double y = getY() - getYOffset();
				double z = getZ() + (Math.random() - 0.5) * getWidth();
				float grav = -0.15F - (float) Math.random() * 0.03F;
				Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
			}
		}

	}

	@Override
	public Vector3d getLookVec() {
		float x = dataManager.get(LOOK_X);
		float y = dataManager.get(LOOK_Y);
		float z = dataManager.get(LOOK_Z);
		return new Vector3d(x, y, z);
	}

	public int getTimeAlive() {
		return dataManager.get(TIME_ALIVE);
	}

	public void setTimeAlive(int i) {
		dataManager.set(TIME_ALIVE, i);
	}

	@Nullable
	public LivingEntity getCaster() {
		return dataManager.get(CASTER_UUID).map(getEntityWorld()::getPlayerByUuid).orElse(null);
	}

	@Override
	public boolean isImmune() {
		return true;
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
