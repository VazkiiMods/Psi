/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [19/02/2016, 18:32:25 (GMT)]
 */
package vazkii.psi.common.entity;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySpellMine extends EntitySpellGrenade {

	boolean triggered = false;
	
	public EntitySpellMine(World worldIn) {
		super(worldIn);
	}

	public EntitySpellMine(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(1, 1, 1));
		EntityLivingBase thrower = getThrower();
		if(thrower != null && entities.contains(thrower))
			entities.remove(thrower);
		
		if(!entities.isEmpty())
			triggered = true;
		else if(triggered)
			doExplosion();
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
