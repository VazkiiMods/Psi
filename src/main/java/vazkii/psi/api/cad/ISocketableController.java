/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 23:22:12 (GMT)]
 */
package vazkii.psi.api.cad;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Base interface for an item that can control other ISocketables, such as the
 * Exosuit Controller.
 */
public interface ISocketableController {

	/**
	 * Returns an array of stacks this item can control. Can't be null.
	 * Elements can be empty. The item of every non-empty stack must provide an ISocketableCapability.
	 */
	ItemStack[] getControlledStacks(PlayerEntity player, ItemStack stack);

	int getDefaultControlSlot(ItemStack stack);

	void setSelectedSlot(PlayerEntity player, ItemStack stack, int controlSlot, int slot);

}
