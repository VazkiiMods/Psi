/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 17:21:21 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADComponent;

public class SlotCADComponent extends Slot {

	EnumCADComponent componentType;
	
	public SlotCADComponent(IInventory inventoryIn, int index, int xPosition, int yPosition, EnumCADComponent componentType) {
		super(inventoryIn, index, xPosition, yPosition);
		this.componentType = componentType;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack != null && stack.getItem() instanceof ICADComponent) {
			EnumCADComponent type = ((ICADComponent) stack.getItem()).getComponentType(stack);
			return type == componentType;
		}
		
		return false; 
	}

}
