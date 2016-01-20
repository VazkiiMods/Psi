/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
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
		"cadCoreOverclocked",
		"cadCoreConductive",
		"cadCoreHyperclocked",
		"cadCoreRadiative"
	};
	
	public ItemCADCore() {
		super(LibItemNames.CAD_CORE, VARIANTS);
	}
	
	@Override
	public void registerStats() {
		// Basic
		addStat(EnumCADStat.COMPLEXITY, 0, 6);
		addStat(EnumCADStat.PROJECTION, 0, 1);
		
		// Overclocked
		addStat(EnumCADStat.COMPLEXITY, 1, 14);
		addStat(EnumCADStat.PROJECTION, 1, 3);
		
		// Conductive
		addStat(EnumCADStat.COMPLEXITY, 2, 12);
		addStat(EnumCADStat.PROJECTION, 2, 4);
		
		// Hyperclocked
		addStat(EnumCADStat.COMPLEXITY, 3, 20);
		addStat(EnumCADStat.PROJECTION, 3, 5);
		
		// Radiative
		addStat(EnumCADStat.COMPLEXITY, 4, 16);
		addStat(EnumCADStat.PROJECTION, 4, 6);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.CORE;
	}

}
