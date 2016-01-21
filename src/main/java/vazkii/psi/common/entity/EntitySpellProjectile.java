/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [20/01/2016, 23:10:47 (GMT)]
 */
package vazkii.psi.common.entity;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.Psi;

public class EntitySpellProjectile extends EntityThrowable {
	
	private static final String TAG_COLORIZER = "colorizer";
	private static final String TAG_BULLET = "bullet";
	
    public EntitySpellProjectile(World worldIn) {
        super(worldIn);
        setSize(0F, 0F);
    }

    public EntitySpellProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        
        double speed = 1.5;
        motionX *= speed;
        motionY *= speed;
        motionZ *= speed;
    }
    
    public EntitySpellProjectile setInfo(EntityPlayer player, ItemStack colorizer, ItemStack bullet) {
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
    	dataWatcher.setObjectWatched(20);
    	dataWatcher.setObjectWatched(21);
    	dataWatcher.setObjectWatched(22);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
    	super.writeEntityToNBT(tagCompound);
    	
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
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
    	super.readEntityFromNBT(tagCompund);
    	
    	NBTTagCompound colorizerCmp = tagCompund.getCompoundTag(TAG_COLORIZER);
    	ItemStack colorizer = ItemStack.loadItemStackFromNBT(colorizerCmp);
    	dataWatcher.updateObject(20, colorizer);
    	
    	NBTTagCompound bulletCmp = tagCompund.getCompoundTag(TAG_BULLET);
    	ItemStack bullet = ItemStack.loadItemStackFromNBT(bulletCmp);
    	dataWatcher.updateObject(21, bullet);
    	
    	EntityLivingBase thrower = getThrower();
    	if(thrower != null && thrower instanceof EntityPlayer)
    		dataWatcher.updateObject(22, ((EntityPlayer) thrower).getName());
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	int colorVal = ICADColorizer.DEFAULT_SPELL_COLOR;
    	ItemStack colorizer = dataWatcher.getWatchableObjectItemStack(20);
    	if(colorizer != null && colorizer.getItem() instanceof ICADColorizer)
    		colorVal = ((ICADColorizer) colorizer.getItem()).getColor(colorizer);
    	
		Color color = new Color(colorVal);
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;
    	
		double x = posX;
		double y = posY;
		double z = posZ;
		Vector3 lookOrig = new Vector3(motionX, motionY, motionZ).normalize();
		for(int i = 0; i < 5; i++) {
			Vector3 look = lookOrig.copy();
			double spread = 0.6;
			look.x += (Math.random() - 0.5) * spread;
			look.y += (Math.random() - 0.5) * spread;
			look.z += (Math.random() - 0.5) * spread;
			look.normalize().multiply(0.15);
			
			Psi.proxy.sparkleFX(worldObj, x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 1.2F, 12);
		}
    }
    
	@Override
	protected void onImpact(MovingObjectPosition pos) {
		SpellContext context = null;
		Entity thrower = getThrower();
		System.out.println("thrower: " + thrower);
		if(thrower != null && thrower instanceof EntityPlayer) {
			ItemStack spellContainer = dataWatcher.getWatchableObjectItemStack(21);
			if(spellContainer != null && spellContainer.getItem() instanceof ISpellContainer) {
				Spell spell = ((ISpellContainer) spellContainer.getItem()).getSpell(spellContainer);
				if(spell != null)
					context = new SpellContext().setPlayer((EntityPlayer) thrower).setFocalPoint(this).setSpell(spell);
			}
		}
		
		try {
			System.out.println(context);
			if(context != null)
				context.cspell.execute(context);
		} catch(SpellRuntimeException e) {
			if(!context.caster.worldObj.isRemote)
				context.caster.addChatComponentMessage(new ChatComponentTranslation(e.getMessage()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));			
		}
		
		setDead();
	}
	
	@Override
	public EntityLivingBase getThrower() {
		EntityLivingBase superThrower = super.getThrower();
		if(superThrower != null)
			return superThrower;
		
		String name = dataWatcher.getWatchableObjectString(22);
		EntityPlayer player = worldObj.getPlayerEntityByName(name);
		return player;
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
