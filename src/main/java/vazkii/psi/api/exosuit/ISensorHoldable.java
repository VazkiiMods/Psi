/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.exosuit;

import net.minecraft.world.item.ItemStack;

/**
 * An item that implements this can have a sensor attacked to it.
 * What did you expect?
 */
public interface ISensorHoldable {

	ItemStack getAttachedSensor(ItemStack stack);

	void attachSensor(ItemStack stack, ItemStack sensor);

}
