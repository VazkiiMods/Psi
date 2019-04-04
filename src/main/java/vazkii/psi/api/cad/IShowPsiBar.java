/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 23:50:35 (GMT)]
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
