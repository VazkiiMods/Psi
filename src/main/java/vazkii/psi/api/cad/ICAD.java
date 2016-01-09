/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://botaniamod.net/license.php
 * 
 * File Created @ [09/01/2016, 17:08:06 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;

public interface ICAD {

	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);
	
	public int getStatValue(ItemStack stack, EnumCADStat stat);
	
}
