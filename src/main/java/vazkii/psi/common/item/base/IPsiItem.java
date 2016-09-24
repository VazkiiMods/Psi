/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [28/08/2016, 01:43:03 (GMT)]
 */
package vazkii.psi.common.item.base;

import vazkii.arl.interf.IVariantHolder;
import vazkii.psi.common.lib.LibMisc;

public interface IPsiItem extends IVariantHolder {

	@Override
	default String getModNamespace() {
		return LibMisc.RESOURCE_NAMESPACE;
	}
	
}
