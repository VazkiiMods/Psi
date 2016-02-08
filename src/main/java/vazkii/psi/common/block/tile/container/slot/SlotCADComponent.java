/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [10/01/2016, 17:21:21 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;

public class SlotCADComponent extends Slot {

	EnumCADComponent componentType;
	
	public SlotCADComponent(IInventory inventoryIn, int index, int xPosition, int yPosition, EnumCADComponent componentType) {
		super(inventoryIn, index, xPosition, yPosition);
		this.componentType = componentType;
	}
	
	public SlotCADComponent map(Map<EnumCADComponent, Slot> map) {
		map.put(componentType, this);
		return this;
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
