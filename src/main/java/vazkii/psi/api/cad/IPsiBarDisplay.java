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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import vazkii.psi.api.internal.IPlayerData;

/**
 * This interface defines a capability that shows the PSI bar when held, such as a Psimetal tool or a CAD.
 */
public interface IPsiBarDisplay {

	@CapabilityInject(IPsiBarDisplay.class)
	Capability<IPsiBarDisplay> CAPABILITY = null;

	/**
	 * Whether the PSI bar should be shown while holding this stack.
	 */
	boolean shouldShow(IPlayerData data);
}
