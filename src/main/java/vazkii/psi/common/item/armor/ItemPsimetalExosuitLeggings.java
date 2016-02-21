/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [20/02/2016, 22:31:02 (GMT)]
 */
package vazkii.psi.common.item.armor;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalExosuitLeggings extends ItemPsimetalArmor {

	public ItemPsimetalExosuitLeggings() {
		super(LibItemNames.PSIMETAL_EXOSUIT_LEGGINGS, 2);
	}

	@Override
	public void onEvent(ItemStack stack, PsiArmorEvent event) {
		if(event.type.equals(PsiArmorEvent.TICK))
			cast(stack, event);
	}
	
	@Override
	public float getCastVolume() {
		return 0F;
	}
	
}
