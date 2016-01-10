/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 17:46:41 (GMT)]
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;

public class SlotCADOutput extends SlotExtractOnly {

	TileCADAssembler assmbler;
	
	public SlotCADOutput(TileCADAssembler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		assmbler = inventoryIn;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
		super.onPickupFromSlot(playerIn, stack);
		assmbler.onCraftCAD();
	}

}
