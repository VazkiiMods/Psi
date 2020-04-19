/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;

import vazkii.psi.api.internal.IPlayerData;

/**
 * This interface defines an item that shows the PSI bar when held, such as a Psimetal tool or a CAD.
 *
 * As of version 73, this interface should not be used directly,
 * instead interacting with the item via its {@link IPsiBarDisplay}.
 */
public interface IShowPsiBar {
	/**
	 * Whether the PSI bar should be shown while holding this stack.
	 */
	boolean shouldShow(ItemStack stack, IPlayerData data);
}
