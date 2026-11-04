/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.item.base.ModCADComponents;

public class DefaultStats {

	public static void modifyCreativeAssemblyStats(CADStatEvent event) {
		ItemStack cad = event.getCad();
		ICAD cadItem = (ICAD) cad.getItem();
		ItemStack assembly = cadItem.getComponentInSlot(cad, EnumCADComponent.ASSEMBLY);
		if(!assembly.isEmpty() && assembly.getItem() == ModCADComponents.cadAssemblyCreative.get()) {
			switch(event.getStat()) {
			case BANDWIDTH:
				event.setStatValue(9);
				break;
			case SOCKETS:
				event.setStatValue(12);
				break;
			default:
				event.setStatValue(-1);
				break;
			}
		}
	}
}
