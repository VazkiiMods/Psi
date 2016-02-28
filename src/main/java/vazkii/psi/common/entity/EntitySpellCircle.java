/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [30/01/2016, 16:09:44 (GMT)]
 */
package vazkii.psi.common.entity;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;

public class EntitySpellCircle extends Entity {

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
	
	public EntitySpellCircle(World worldIn) {
		super(worldIn);
		setSize(3F, 0F);
	}

	public EntitySpellCircle setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
		dataWatcher.updateObject(20, colorizer);
		dataWatcher.updateObject(21, bullet);
		dataWatcher.updateObject(22, player.getName());
		
		Vec3 lookVec = player.getLook(1F);
		dataWatcher.updateObject(25, (float) lookVec.xCoord);
		dataWatcher.updateObject(26, (float) lookVec.yCoord);
		dataWatcher.updateObject(27, (float) lookVec.zCoord);
		return this;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(20, new ItemStack(Blocks.stone));
		dataWatcher.addObject(21, new ItemStack(Blocks.stone));
		dataWatcher.addObject(22, "");
		dataWatcher.addObject(23, 0);
		dataWatcher.addObject(24, 0);
		dataWatcher.addObject(25, 0F);
		dataWatcher.addObject(26, 0F);
		dataWatcher.addObject(27, 0F);
		for(int i = 0; i < 8; i++)
			dataWatcher.setObjectWatched(20 + i);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		NBTTagCompound colorizerCmp = new NBTTagCompound();
		ItemStack colorizer = dataWatcher.getWatchableObjectItemStack(20);
		if(colorizer != null)
			colorizer.writeToNBT(colorizerCmp);
		tagCompound.setTag(TAG_COLORIZER, colorizerCmp);

		NBTTagCompound bulletCmp = new NBTTagCompound();
		ItemStack bullet = dataWatcher.getWatchableObjectItemStack(21);
		if(bullet != null)
			bullet.writeToNBT(bulletCmp);
		tagCompound.setTag(TAG_BULLET, bulletCmp);

		tagCompound.setString(TAG_CASTER, dataWatcher.getWatchableObjectString(22));
		tagCompound.setInteger(TAG_TIME_ALIVE, getTimeAlive());
		tagCompound.setInteger(TAG_TIMES_CAST, dataWatcher.getWatchableObjectInt(24));
		
		tagCompound.setFloat(TAG_LOOK_X, dataWatcher.getWatchableObjectFloat(25));
		tagCompound.setFloat(TAG_LOOK_Y, dataWatcher.getWatchableObjectFloat(26));
		tagCompound.setFloat(TAG_LOOK_Z, dataWatcher.getWatchableObjectFloat(27));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		NBTTagCompound colorizerCmp = tagCompound.getCompoundTag(TAG_COLORIZER);
		ItemStack colorizer = ItemStack.loadItemStackFromNBT(colorizerCmp);
		dataWatcher.updateObject(20, colorizer);

		NBTTagCompound bulletCmp = tagCompound.getCompoundTag(TAG_BULLET);
		ItemStack bullet = ItemStack.loadItemStackFromNBT(bulletCmp);
		dataWatcher.updateObject(21, bullet);

		dataWatcher.updateObject(22, tagCompound.getString(TAG_CASTER));
		setTimeAlive(tagCompound.getInteger(TAG_TIME_ALIVE));
		dataWatcher.updateObject(24, tagCompound.getInteger(TAG_TIMES_CAST));
		
		dataWatcher.updateObject(25, tagCompound.getFloat(TAG_LOOK_X));
		dataWatcher.updateObject(26, tagCompound.getFloat(TAG_LOOK_Y));
		dataWatcher.updateObject(27, tagCompound.getFloat(TAG_LOOK_Z));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int timeAlive = getTimeAlive();
		if(timeAlive > LIVE_TIME)
			setDead();

		setTimeAlive(timeAlive + 1);
		int times = dataWatcher.getWatchableObjectInt(24);

		if(timeAlive > CAST_DELAY && timeAlive % CAST_DELAY == 0 && times < 20) {
			SpellContext context = null;
			Entity thrower = getCaster();
			if(thrower != null && thrower instanceof EntityPlayer) {
				ItemStack spellContainer = dataWatcher.getWatchableObjectItemStack(21);
				if(spellContainer != null && spellContainer.getItem() instanceof ISpellContainer) {
					dataWatcher.updateObject(24, times + 1);
					Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
					if(spell != null)
						context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell).setLoopcastIndex(times);
				}
			}

			if(context != null)
				context.cspell.safeExecute(context);
		}

		int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
		ItemStack colorizer = dataWatcher.getWatchableObjectItemStack(20);
		if(colorizer != null && colorizer.getItem() instanceof ICADColorizer)
			colorVal = Psi.proxy.getColorizerColor(colorizer).getRGB();

		Color color = new Color(colorVal);
		float r = color.getRed() / 255F;
		float g = color.getGreen() / 255F;
		float b = color.getBlue() / 255F;
		for(int i = 0; i < 5; i++) {
			double x = posX + (Math.random() - 0.5) * width;
			double y = posY - getYOffset();
			double z = posZ + (Math.random() - 0.5) * width;
			float grav = -0.15F - (float) Math.random() * 0.03F;
			Psi.proxy.sparkleFX(worldObj, x, y, z, r, g, b, grav, 0.25F, 15);
		}
	}
	
	@Override
	public Vec3 getLook(float f) {
		float x = dataWatcher.getWatchableObjectFloat(25);
		float y = dataWatcher.getWatchableObjectFloat(26);
		float z = dataWatcher.getWatchableObjectFloat(27);
		return new Vec3(x, y, z);
	}

	public int getTimeAlive() {
		return dataWatcher.getWatchableObjectInt(23);
	}

	public void setTimeAlive(int i) {
		dataWatcher.updateObject(23, i);
	}

	public EntityLivingBase getCaster() {
		String name = dataWatcher.getWatchableObjectString(22);
		EntityPlayer player = worldObj.getPlayerEntityByName(name);
		return player;
	}
}
