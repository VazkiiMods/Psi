/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 21:14:58 (GMT)]
 */
package vazkii.psi.common.item.tool;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.core.helper.ItemNBTHelper;

public interface IPsimetalTool extends ISocketable, ISpellSettable {

	public static final String TAG_BULLET_PREFIX = "bullet";
	public static final String TAG_SELECTED_SLOT = "selectedSlot";

	@Override
	public default boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		return slot < 3;
	}

	@Override
	public default boolean showSlotInRadialMenu(ItemStack stack, int slot) {
		return isSocketSlotAvailable(stack, slot - 1);
	}

	@Override
	public default ItemStack getBulletInSocket(ItemStack stack, int slot) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);

		if(cmp == null)
			return null;

		return ItemStack.loadItemStackFromNBT(cmp);
	}

	@Override
	public default void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = new NBTTagCompound();

		if(bullet != null)
			bullet.writeToNBT(cmp);

		ItemNBTHelper.setCompound(stack, name, cmp);
	}

	@Override
	public default int getSelectedSlot(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
	}

	@Override
	public default void setSelectedSlot(ItemStack stack, int slot) {
		ItemNBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
	}

	@Override
	public default void setSpell(ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack bullet = getBulletInSocket(stack, slot);
		if(bullet != null && bullet.getItem() instanceof ISpellSettable) {
			((ISpellSettable) bullet.getItem()).setSpell(bullet, spell);
			setBulletInSocket(stack, slot, bullet);
		}
	}

}
