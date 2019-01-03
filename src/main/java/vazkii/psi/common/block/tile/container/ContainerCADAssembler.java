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
import vazkii.psi.api.inventory.InventorySocketable;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.slot.InventoryAssemblerOutput;
import vazkii.psi.common.block.tile.container.slot.SlotCADOutput;
import vazkii.psi.common.block.tile.container.slot.SlotSocketable;
import vazkii.psi.common.block.tile.container.slot.ValidatorSlot;

import javax.annotation.Nonnull;

public class ContainerCADAssembler extends Container {

	private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

	public final TileCADAssembler assembler;

	private final int cadComponentStart;
	private final int socketableStart;
	private final int socketableEnd;
	private final int bulletStart;
	private final int bulletEnd;
	private final int playerStart;
	private final int playerEnd;
	private final int hotbarStart;
	private final int hotbarEnd;
	private final int armorStart;

	public ContainerCADAssembler(EntityPlayer player, TileCADAssembler assembler) {
		InventoryPlayer playerInventory = player.inventory;
		int playerSize = playerInventory.getSizeInventory();

		this.assembler = assembler;
		assembler.clearCachedCAD();

		InventoryAssemblerOutput output = new InventoryAssemblerOutput(player, assembler);
		InventorySocketable bullets = new InventorySocketable(assembler.getSocketableStack());

		addSlotToContainer(new SlotCADOutput(output, assembler, 120, 35));

		cadComponentStart = inventorySlots.size();
		addSlotToContainer(new ValidatorSlot(assembler, assembler.getComponentSlot(EnumCADComponent.ASSEMBLY), 120, 91));
		addSlotToContainer(new ValidatorSlot(assembler, assembler.getComponentSlot(EnumCADComponent.CORE), 100, 91));
		addSlotToContainer(new ValidatorSlot(assembler, assembler.getComponentSlot(EnumCADComponent.SOCKET), 140, 91));
		addSlotToContainer(new ValidatorSlot(assembler, assembler.getComponentSlot(EnumCADComponent.BATTERY), 110, 111));
		addSlotToContainer(new ValidatorSlot(assembler, assembler.getComponentSlot(EnumCADComponent.DYE), 130, 111));

		socketableStart = inventorySlots.size();
		addSlotToContainer(new SlotSocketable(assembler, bullets, 0, 35, 21));
		socketableEnd = inventorySlots.size();


		bulletStart = inventorySlots.size();
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 3; col++)
				addSlotToContainer(new ValidatorSlot(bullets, col + row * 3, 17 + col * 18, 57 + row * 18));
		bulletEnd = inventorySlots.size();

		int xs = 48;
		int ys = 143;

		playerStart = inventorySlots.size();
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 9; col++)
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, xs + col * 18, ys + row * 18));
		playerEnd = inventorySlots.size();

		hotbarStart = inventorySlots.size();
		for (int col = 0; col < 9; col++)
			addSlotToContainer(new Slot(playerInventory, col, xs + col * 18, ys + 58));
		hotbarEnd = inventorySlots.size();

		armorStart = inventorySlots.size();
		for (int armorSlot = 0; armorSlot < 4; armorSlot++) {
			final EntityEquipmentSlot slot = equipmentSlots[armorSlot];

			addSlotToContainer(new Slot(playerInventory, playerSize - 2 - armorSlot,
					xs - 27, ys + 18 * armorSlot) {
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

		addSlotToContainer(new Slot(playerInventory, playerSize - 1, 219, 143) {
			@SideOnly(Side.CLIENT)
			@Override
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
		return assembler.isUsableByPlayer(playerIn);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int from) {
		ItemStack mergeStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(from);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			mergeStack = stackInSlot.copy();

			if (from >= playerStart) {
				if (stackInSlot.getItem() instanceof ICADComponent) {
					EnumCADComponent componentType = ((ICADComponent) stackInSlot.getItem()).getComponentType(stackInSlot);
					int componentSlot = cadComponentStart + componentType.ordinal();
					if (!mergeItemStack(stackInSlot, componentSlot, componentSlot + 1, false))
						return ItemStack.EMPTY;
				} else if (stackInSlot.getItem() instanceof ISocketable) {
					if (!mergeItemStack(stackInSlot, socketableStart, socketableEnd, false))
						return ItemStack.EMPTY;
				} else if (stackInSlot.getItem() instanceof ISpellSettable) {
					if (!mergeItemStack(stackInSlot, bulletStart, bulletEnd, false))
						return ItemStack.EMPTY;
				} else if (from < hotbarStart) {
					if (!mergeItemStack(stackInSlot, hotbarStart, hotbarEnd, true))
						return ItemStack.EMPTY;
				} else if (!mergeItemStack(stackInSlot, playerStart, playerEnd, false))
					return ItemStack.EMPTY;
			} else if (stackInSlot.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) stackInSlot.getItem();
				int armorSlot = armorStart + armor.armorType.getSlotIndex() - 1;
				if (!mergeItemStack(stackInSlot, armorSlot, armorSlot + 1, true) &&
						!mergeItemStack(stackInSlot, playerStart, hotbarEnd, true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(stackInSlot, playerStart, hotbarEnd, true))
				return ItemStack.EMPTY;

			slot.onSlotChanged();

			if (stackInSlot.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else if (stackInSlot.getCount() == mergeStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, stackInSlot);
		}

		return mergeStack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		assembler.clearCachedCAD();
	}
}
