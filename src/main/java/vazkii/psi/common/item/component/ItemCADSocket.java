/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 00:17:17 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCADSocket extends ItemCADComponent {

	public static final String[] VARIANTS = {
		"cadSocketBasic",
	};
	
	public ItemCADSocket() {
		super(LibItemNames.CAD_SOCKET, VARIANTS);
	}
	
	@Override
	public void registerStats() {
		addStat(EnumCADStat.BANDWIDTH, 0, 5);
		addStat(EnumCADStat.SOCKETS, 0, 4);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.SOCKET;
	}

}
