/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://botaniamod.net/license.php
 * 
 * File Created @ [09/01/2016, 01:09:40 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;

public interface ICADComponent {

	public EnumCADComponent getComponentType(ItemStack stack);
	
	public int getCADStatValue(ItemStack stack, EnumCADStat stat);
	
}
