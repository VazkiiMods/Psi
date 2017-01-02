/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 22:51:28 (GMT)]
 */
package vazkii.psi.common.block.base;

import vazkii.arl.interf.IModBlock;
import vazkii.psi.common.lib.LibMisc;

public interface IPsiBlock extends IModBlock {

	@Override
	default String getModNamespace() {
		return LibMisc.MOD_ID;
	}

}
