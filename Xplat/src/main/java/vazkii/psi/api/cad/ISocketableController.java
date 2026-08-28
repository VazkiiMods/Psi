/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Base interface for an item that can control other ISocketables, such as the
 * Exosuit Controller.
 */
public interface ISocketableController {

	/**
	 * Returns an array of stacks this item can control. Can't be null.
	 * Elements can be empty. The item of every non-empty stack must provide an ISocketableCapability.
	 */
	ItemStack[] getControlledStacks(Player player, ItemStack stack);

	int getDefaultControlSlot(ItemStack stack);

	void setSelectedSlot(Player player, ItemStack stack, int controlSlot, int slot);

}
