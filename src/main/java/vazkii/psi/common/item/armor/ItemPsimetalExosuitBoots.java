/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 22:31:19 (GMT)]
 */
package vazkii.psi.common.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.lib.LibItemNames;

public class ItemPsimetalExosuitBoots extends ItemPsimetalArmor {

	public ItemPsimetalExosuitBoots() {
		super(LibItemNames.PSIMETAL_EXOSUIT_BOOTS, 3, EntityEquipmentSlot.FEET);
	}

	@Override
	public String getEvent(ItemStack stack) {
		return PsiArmorEvent.JUMP;
	}

}
