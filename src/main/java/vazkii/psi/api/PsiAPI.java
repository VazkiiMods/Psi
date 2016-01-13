/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 16:08:02 (GMT)]
 */
package vazkii.psi.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ICAD;

public final class PsiAPI {
	
	// TODO internal handler

	public static ItemStack getPlayerCAD(EntityPlayer player) {
		if(player == null)
			return null;
		
		ItemStack cad = null;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if(stackAt != null && stackAt.getItem() instanceof ICAD) {
				if(cad != null)
					return null; // Player can only have one CAD
				
				cad = stackAt;
			}
		}
		
		return cad;
	}
	
}
