/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/02/2016, 16:42:36 (GMT)]
 */
package vazkii.psi.api.exosuit;

import net.minecraft.item.ItemStack;

/**
 * An item that implements this can have a sensor attacked to it.
 * What did you expect?
 */
public interface ISensorHoldable {

	ItemStack getAttachedSensor(ItemStack stack);

	void attachSensor(ItemStack stack, ItemStack sensor);

}
