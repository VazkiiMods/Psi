/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 18:05:24 (GMT)]
 */
package vazkii.psi.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySpellCharge extends EntitySpellGrenade {

	public EntitySpellCharge(World worldIn) {
		super(worldIn);
	}

	public EntitySpellCharge(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	public int getParticleCount() {
		return 2;
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
