/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://botaniamod.net/license.php
 * 
 * File Created @ [09/01/2016, 00:17:09 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADCore extends ItemCADComponent {

	public static final String[] VARIANTS = {
		"cadCoreBasic",
	};
	
	public ItemCADCore() {
		super(LibItemNames.CAD_CORE, VARIANTS);
	}
	
	@Override
	public void registerStats() {
		addStat(EnumCADStat.COMPLEXITY, 0, 6);
		addStat(EnumCADStat.PROJECTION, 0, 1);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.CORE;
	}

}
