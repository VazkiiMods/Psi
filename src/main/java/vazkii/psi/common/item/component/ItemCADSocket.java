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
		"cadSocketSignaling",
		"cadSocketLarge",
		"cadSocketTransmissive",
		"cadSocketHuge"
	};
	
	public ItemCADSocket() {
		super(LibItemNames.CAD_SOCKET, VARIANTS);
	}
	
	@Override
	public void registerStats() {
		// Basic
		addStat(EnumCADStat.BANDWIDTH, 0, 5);
		addStat(EnumCADStat.SOCKETS, 0, 4);
		
		// Signaling
		addStat(EnumCADStat.BANDWIDTH, 1, 7);
		addStat(EnumCADStat.SOCKETS, 1, 6);
		
		// Large
		addStat(EnumCADStat.BANDWIDTH, 2, 6);
		addStat(EnumCADStat.SOCKETS, 2, 8);
		
		// Transmissive
		addStat(EnumCADStat.BANDWIDTH, 3, 9);
		addStat(EnumCADStat.SOCKETS, 3, 10);
		
		// Huge
		addStat(EnumCADStat.BANDWIDTH, 4, 8);
		addStat(EnumCADStat.SOCKETS, 4, 12);
	}

	@Override
	public EnumCADComponent getComponentType(ItemStack stack) {
		return EnumCADComponent.SOCKET;
	}

}
