/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 18:38:54 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;

/**
 * This interface defines an item that can have Spell Bullets
 * put into it.
 */
public interface ISocketable {

	public static final int MAX_SLOTS = 12;

	public static String getSocketedItemName(ItemStack stack, String fallback) {
		if(stack == null || !(stack.getItem() instanceof ISocketable))
			return fallback;

		ISocketable socketable = (ISocketable) stack.getItem();
		ItemStack item = socketable.getBulletInSocket(stack, socketable.getSelectedSlot(stack));
		if(item == null)
			return fallback;

		return item.getDisplayName();
	}

	/**
	 * Gets if the passed in slot is available for inserting bullets given the ItemStack passed in.
	 */
	public boolean isSocketSlotAvailable(ItemStack stack, int slot);

	/**
	 * Gets whether the passed in slot should be shown in the radial menu.
	 */
	public default boolean showSlotInRadialMenu(ItemStack stack, int slot) {
		return isSocketSlotAvailable(stack, slot);
	}

	/**
	 * Gets the bullet in the slot passed in. Can be null.
	 */
	public ItemStack getBulletInSocket(ItemStack stack, int slot);

	/**
	 * Sets the bullet in the slot passed in.
	 */
	public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet);

	/**
	 * Gets the slot that is currently selected.
	 */
	public int getSelectedSlot(ItemStack stack);

	/**
	 * Sets ths currently selected slot.
	 */
	public void setSelectedSlot(ItemStack stack, int slot);

}
