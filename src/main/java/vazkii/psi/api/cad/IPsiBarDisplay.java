/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import vazkii.psi.api.internal.IPlayerData;

/**
 * This interface defines a capability that shows the PSI bar when held, such as a Psimetal tool or a CAD.
 */
public interface IPsiBarDisplay {

	/**
	 * Whether the PSI bar should be shown while holding this stack.
	 */
	boolean shouldShow(IPlayerData data);
}
