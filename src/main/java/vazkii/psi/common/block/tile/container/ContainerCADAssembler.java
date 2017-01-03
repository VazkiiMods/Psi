/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 16:52:47 (GMT)]
 */
package vazkii.psi.common.block.tile.container;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import vazkii.psi.common.block.tile.container.slot.SlotCADComponent;
import vazkii.psi.common.block.tile.container.slot.SlotCADOutput;
import vazkii.psi.common.block.tile.container.slot.SlotSocketable;
import vazkii.psi.common.item.base.ModItems;

public class ContainerCADAssembler extends Container {

    private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	
	public TileCADAssembler assembler;
	private Map<EnumCADComponent, Slot> componentToSlotMap = new HashMap();

	public ContainerCADAssembler(EntityPlayer player, TileCADAssembler assembler) {
		InventoryPlayer playerInventory = player.inventory;
		this.assembler = assembler;

		addSlotToContainer(new SlotCADOutput(assembler, 0, 120, 35));
		addSlotToContainer(new SlotCADComponent(assembler, 1, 100, 91, EnumCADComponent.CORE).map(componentToSlotMap));
		addSlotToContainer(new SlotCADComponent(assembler, 2, 120, 91, EnumCADComponent.ASSEMBLY).map(componentToSlotMap));
		addSlotToContainer(new SlotCADComponent(assembler, 3, 140, 91, EnumCADComponent.SOCKET).map(componentToSlotMap));
		addSlotToContainer(new SlotCADComponent(assembler, 4, 110, 111, EnumCADComponent.BATTERY).map(componentToSlotMap));
		addSlotToContainer(new SlotCADComponent(assembler, 5, 130, 111, EnumCADComponent.DYE).map(componentToSlotMap));

		addSlotToContainer(new SlotSocketable(assembler, 6, 35, 21));

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 3; j++) {
				int slot = j + i * 3;
				addSlotToContainer(new SlotBullet(assembler, slot + 7, 17 + j * 18, 57 + i * 18, slot));
			}

		int xs = 48;
		int ys = 143;

		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, xs + j * 18, ys + i * 18));

		for(int k = 0; k < 9; k++)
			addSlotToContainer(new Slot(playerInventory, k, xs + k * 18, ys + 58));

		for(int k = 0; k < 4; k++) {
            final EntityEquipmentSlot slot = equipmentSlots[k];

			addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 2 - k, xs - 27, ys + 18 * k) {

				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					return !stack.isEmpty() && stack.getItem().isValidArmor(stack, slot, player);
				}

				@SideOnly(Side.CLIENT)
				@Override
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
				}
			});
		}
		
        final EntityEquipmentSlot slot = EntityEquipmentSlot.OFFHAND;
		addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1, 219, 143) {

			@SideOnly(Side.CLIENT)
			@Override
			public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return assembler.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int invStart = 18;
			int hotbarStart = invStart + 28;
			int invEnd = hotbarStart + 9;

			merge: {
				if(index > invStart) {
					if(itemstack1.getItem() instanceof ICADComponent) { // Component slots
						ICADComponent component = (ICADComponent) itemstack1.getItem();
						Slot compSlot = componentToSlotMap.get(component.getComponentType(itemstack1));
						if(!mergeItemStack(itemstack1, compSlot.slotNumber, compSlot.slotNumber + 1, false))
							return ItemStack.EMPTY;
					} else if(itemstack1.getItem() instanceof ISocketable) { // CAD Input slot
						if(!mergeItemStack(itemstack1, 6, 7, false))
							return ItemStack.EMPTY;
					} else if(itemstack1.getItem() == ModItems.spellBullet) {
						if(!mergeItemStack(itemstack1, 7, 19, false))
							return ItemStack.EMPTY;
					} else if(index >= invStart && index < hotbarStart)  { // Inv -> Hotbar
						if (!mergeItemStack(itemstack1, hotbarStart, invEnd, true))
							return ItemStack.EMPTY;
					} else if(index >= hotbarStart && index < invEnd) { // Hotbar -> inv
						if(!mergeItemStack(itemstack1, invStart, hotbarStart, false))
							return ItemStack.EMPTY;
					}
					break merge;
				} 
				
				if(itemstack1.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) itemstack1.getItem();
					int armorSlot = 3 - armor.armorType.getIndex();
					if(!mergeItemStack(itemstack1, invEnd + armorSlot, invEnd + armorSlot + 1, true) && !mergeItemStack(itemstack1, invStart, invEnd, true)) // Assembler -> Armor+Inv+Hotbar
						return ItemStack.EMPTY;
					
					break merge;
				}

				if(!mergeItemStack(itemstack1, invStart, invEnd, true)) // Assembler -> Inv+hotbar
					return ItemStack.EMPTY;
			}

			if(itemstack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

}
