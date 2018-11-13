/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/01/2016, 23:10:47 (GMT)]
 */
package vazkii.psi.common.entity;

import java.awt.Color;
import java.util.function.Consumer;

import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntitySpellProjectile extends EntityThrowable {

	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	private static final String TAG_TIME_ALIVE = "timeAlive";

	private static final String TAG_LAST_MOTION_X = "lastMotionX";
	private static final String TAG_LAST_MOTION_Y = "lastMotionY";
	private static final String TAG_LAST_MOTION_Z = "lastMotionZ";

	private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntitySpellProjectile.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<ItemStack> BULLET_DATA = EntityDataManager.createKey(EntitySpellProjectile.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<String> CASTER_NAME = EntityDataManager.createKey(EntitySpellProjectile.class, DataSerializers.STRING);

	public SpellContext context;
	public int timeAlive;

	public EntitySpellProjectile(World worldIn) {
		super(worldIn);
		setSize(0F, 0F);
	}

	public EntitySpellProjectile(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
		
		setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
		double speed = 1.5;
		motionX *= speed;
		motionY *= speed;
		motionZ *= speed;
	}

	public EntitySpellProjectile setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
		dataManager.set(COLORIZER_DATA, colorizer);
		dataManager.set(BULLET_DATA, bullet);
		dataManager.set(CASTER_NAME, player.getName());
		return this;
	}

	@Override
	protected void entityInit() {
		dataManager.register(COLORIZER_DATA, new ItemStack(Blocks.STONE));
		dataManager.register(BULLET_DATA, new ItemStack(Blocks.STONE));
		dataManager.register(CASTER_NAME, "");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);

		NBTTagCompound colorizerCmp = new NBTTagCompound();
		ItemStack colorizer = dataManager.get(COLORIZER_DATA);
		if(!colorizer.isEmpty())
			colorizer.writeToNBT(colorizerCmp);
		tagCompound.setTag(TAG_COLORIZER, colorizerCmp);

		NBTTagCompound bulletCmp = new NBTTagCompound();
		ItemStack bullet = dataManager.get(BULLET_DATA);
		if(!bullet.isEmpty())
			bullet.writeToNBT(bulletCmp);
		tagCompound.setTag(TAG_BULLET, bulletCmp);

		tagCompound.setInteger(TAG_TIME_ALIVE, timeAlive);

		tagCompound.setDouble(TAG_LAST_MOTION_X, motionX);
		tagCompound.setDouble(TAG_LAST_MOTION_Y, motionY);
		tagCompound.setDouble(TAG_LAST_MOTION_Z, motionZ);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);

		NBTTagCompound colorizerCmp = tagCompound.getCompoundTag(TAG_COLORIZER);
		ItemStack colorizer = new ItemStack(colorizerCmp);
		dataManager.set(COLORIZER_DATA, colorizer);

		NBTTagCompound bulletCmp = tagCompound.getCompoundTag(TAG_BULLET);
		ItemStack bullet = new ItemStack(bulletCmp);
		dataManager.set(BULLET_DATA, bullet);

		EntityLivingBase thrower = getThrower();
		if(thrower != null && thrower instanceof EntityPlayer)
			dataManager.set(CASTER_NAME, ((EntityPlayer) thrower).getName());

		timeAlive = tagCompound.getInteger(TAG_TIME_ALIVE);

		double lastMotionX = tagCompound.getDouble(TAG_LAST_MOTION_X);
		double lastMotionY = tagCompound.getDouble(TAG_LAST_MOTION_Y);
		double lastMotionZ = tagCompound.getDouble(TAG_LAST_MOTION_Z);
		motionX = lastMotionX;
		motionY = lastMotionY;
		motionZ = lastMotionZ;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		int timeAlive = ticksExisted;
		if(timeAlive > getLiveTime())
			setDead();
		timeAlive++;
		
		int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack colorizer = dataManager.get(COLORIZER_DATA);
		if(!colorizer.isEmpty() && colorizer.getItem() instanceof ICADColorizer)
			colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();

		Color color = new Color(colorVal);
		float r = color.getRed() / 255F;
		float g = color.getGreen() / 255F;
		float b = color.getBlue() / 255F;

		double x = posX;
		double y = posY;
		double z = posZ;
		
		Vector3 lookOrig = new Vector3(motionX, motionY, motionZ).normalize();
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

			Psi.proxy.sparkleFX(getEntityWorld(), x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 1.2F, 12);
		}
	}

	public int getLiveTime() {
		return 600;
	}

	public int getParticleCount() {
		return 5;
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if(pos.entityHit != null && pos.entityHit instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase) pos.entityHit; // apparently RayTraceResult is mutable \:D/
			cast((SpellContext context) -> {
				if (context != null) {
					context.attackedEntity = e;
				}
			});
		} else cast();
	}

	public void cast() {
		cast(null);
	}

	public void cast(Consumer<SpellContext> callback) {
		Entity thrower = getThrower();
		boolean canCast = false;

		if(thrower != null && thrower instanceof EntityPlayer) {
			ItemStack spellContainer = dataManager.get(BULLET_DATA);
			if(spellContainer != null && spellContainer.getItem() instanceof ISpellContainer) {
				Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
				if(spell != null) {
					canCast = true;
					if(context == null)
						context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell);
					context.setFocalPoint(this);
				}
			}
		}

		if(callback != null)
			callback.accept(context);

		if(canCast && context != null)
			context.cspell.safeExecute(context);

		setDead();
	}

	@Override
	public EntityLivingBase getThrower() {
		EntityLivingBase superThrower = super.getThrower();
		if(superThrower != null)
			return superThrower;

		String name = (String) dataManager.get(CASTER_NAME);
		EntityPlayer player = getEntityWorld().getPlayerEntityByName(name);
		return player;
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
