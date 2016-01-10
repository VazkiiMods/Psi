/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 15:52:20 (GMT)]
 */
package vazkii.psi.common.block.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibBlockNames;

public class TileCADAssembler extends TileSimpleInventory {
	
	boolean ignoreCad = false;
	
	@Override
	public void inventoryChanged(int i) {
		if(i != 0 && !ignoreCad) {
			ItemStack cad = null;
			if(getStackInSlot(2) != null)
				cad = ItemCAD.makeCAD(getStackInSlot(1), getStackInSlot(2), getStackInSlot(3), getStackInSlot(4), getStackInSlot(5));
			
			setInventorySlotContents(0, cad);
		}
 	}
	
	public void onCraftCAD() {
		ignoreCad = true;
		for(int i = 1; i < 6; i++)
			setInventorySlotContents(i, null);
		ignoreCad = false;
	}
	
	@Override
	public int getSizeInventory() {
		return 19;
	}
	
	@Override
	public String getName() {
		return LibBlockNames.CAD_ASSEMBLER;
	}
	
	@Override
	public boolean isAutomationEnabled() {
		return false;
	}

}
