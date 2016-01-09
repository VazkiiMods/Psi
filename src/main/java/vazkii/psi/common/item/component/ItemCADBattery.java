/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * {todo-put-license-here}
 * 
 * File Created @ [09/01/2016, 00:16:37 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADBattery extends ItemCADComponent {

	public static final String[] VARIANTS = {
		"cadBatteryBasic",
	};
	
	public ItemCADBattery() {
		super(LibItemNames.CAD_BATTERY, VARIANTS);
	}
	
	@Override
	public void registerStats() {
		addStat(EnumCADStat.OVERFLOW, 0, 100);
	}
	
	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.BATTERY;
	}

}
