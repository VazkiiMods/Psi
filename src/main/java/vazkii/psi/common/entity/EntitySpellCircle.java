/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.Psi;

public class EntitySpellCircle extends Entity {

	public static final int CAST_TIMES = 20;
	public static final int CAST_DELAY = 5;
	public static final int LIVE_TIME = (CAST_TIMES + 2) * CAST_DELAY;
	
	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	private static final String TAG_CASTER = "caster";
	private static final String TAG_TIME_ALIVE = "timeAlive";

    public EntitySpellCircle(World worldIn) {
        super(worldIn);
        setSize(3F, 0F);
    }

    public EntitySpellCircle setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
    	dataWatcher.updateObject(20, colorizer);
    	dataWatcher.updateObject(21, bullet);
    	dataWatcher.updateObject(22, player.getName());
    	return this;
    }
    
    @Override
    protected void entityInit() {
    	dataWatcher.addObject(20, new ItemStack(Blocks.stone));
    	dataWatcher.addObject(21, new ItemStack(Blocks.stone));
    	dataWatcher.addObject(22, "");
    	dataWatcher.addObject(23, 0);
    	dataWatcher.setObjectWatched(20);
    	dataWatcher.setObjectWatched(21);
    	dataWatcher.setObjectWatched(22);
    	dataWatcher.setObjectWatched(23);
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
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();

    	int timeAlive = getTimeAlive();
    	if(timeAlive > LIVE_TIME)
    		setDead();
    	
    	setTimeAlive(timeAlive + 1);
    	if(timeAlive > CAST_DELAY && timeAlive % CAST_DELAY == 0) {
    		SpellContext context = null;
    		Entity thrower = getCaster();
    		if(thrower != null && thrower instanceof EntityPlayer) {
    			ItemStack spellContainer = dataWatcher.getWatchableObjectItemStack(21);
    			if(spellContainer != null && spellContainer.getItem() instanceof ISpellContainer) {
    				Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
    				if(spell != null)
    					context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell);
    			}
    		}
    		
    		if(context != null)
    			context.cspell.safeExecute(context);
    	}
    	
    	int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
    	ItemStack colorizer = dataWatcher.getWatchableObjectItemStack(20);
    	if(colorizer != null && colorizer.getItem() instanceof ICADColorizer)
    		colorVal = ((ICADColorizer) colorizer.getItem()).getColor(colorizer);
    	
		Color color = new Color(colorVal);
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;
		for(int i = 0; i < 5; i++) {
			double x = posX + (Math.random() - 0.5) * width;
			double y = posY - getYOffset();
			double z = posZ + (Math.random() - 0.5) * width;
			float grav = -0.15F - (float) Math.random() * 0.03F;
			Psi.proxy.sparkleFX(worldObj, x, y, z, r, g, b, grav, 0.25F, 15);
		}
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
