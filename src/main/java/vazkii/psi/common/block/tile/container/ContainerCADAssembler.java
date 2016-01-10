/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 16:52:47 (GMT)]
 */
package vazkii.psi.common.block.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import vazkii.psi.common.block.tile.container.slot.SlotCAD;
import vazkii.psi.common.block.tile.container.slot.SlotCADComponent;
import vazkii.psi.common.block.tile.container.slot.SlotCADOutput;

public class ContainerCADAssembler extends Container {

	public TileCADAssembler assembler;

	public ContainerCADAssembler(InventoryPlayer playerInventory, TileCADAssembler assembler) {
		this.assembler = assembler;

		addSlotToContainer(new SlotCADOutput(assembler, 0, 120, 35));
		addSlotToContainer(new SlotCADComponent(assembler, 1, 100, 91, EnumCADComponent.CORE));
		addSlotToContainer(new SlotCADComponent(assembler, 2, 120, 91, EnumCADComponent.ASSEMBLY));
		addSlotToContainer(new SlotCADComponent(assembler, 3, 140, 91, EnumCADComponent.SOCKET));
		addSlotToContainer(new SlotCADComponent(assembler, 4, 110, 111, EnumCADComponent.BATTERY));
		addSlotToContainer(new SlotCADComponent(assembler, 5, 130, 111, EnumCADComponent.DYE));

		addSlotToContainer(new SlotCAD(assembler, 6, 35, 21));
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 3; j++)
				addSlotToContainer(new SlotBullet(assembler, 7 + j + i * 3, 17 + j * 18, 57 + i * 18));
		
		int xs = 48;
		int ys = 143;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, xs + j * 18, ys + i * 18));

		for(int k = 0; k < 9; k++)
			addSlotToContainer(new Slot(playerInventory, k, xs + k * 18, ys + 58));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return assembler.isUseableByPlayer(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return null; // TODO
	}

}
