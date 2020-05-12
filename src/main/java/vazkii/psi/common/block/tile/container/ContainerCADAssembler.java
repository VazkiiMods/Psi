/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.inventory.InventorySocketable;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.slot.InventoryAssemblerOutput;
import vazkii.psi.common.block.tile.container.slot.SlotCADOutput;
import vazkii.psi.common.block.tile.container.slot.SlotSocketable;
import vazkii.psi.common.block.tile.container.slot.ValidatorSlot;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ContainerCADAssembler extends Container {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibBlockNames.CAD_ASSEMBLER)
	public static ContainerType<ContainerCADAssembler> TYPE;

	private static final EquipmentSlotType[] equipmentSlots = new EquipmentSlotType[] { EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET };

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

	public static ContainerCADAssembler fromNetwork(int windowId, PlayerInventory playerInventory, PacketBuffer buf) {
		BlockPos pos = buf.readBlockPos();
		return new ContainerCADAssembler(windowId, playerInventory, (TileCADAssembler) playerInventory.player.world.getTileEntity(pos));
	}

	public ContainerCADAssembler(int windowId, PlayerInventory playerInventory, TileCADAssembler assembler) {
		super(TYPE, windowId);
		PlayerEntity player = playerInventory.player;
		int playerSize = playerInventory.getSizeInventory();

		this.assembler = assembler;
		IItemHandlerModifiable assemblerInv = assembler.getInventory();
		assembler.clearCachedCAD();

		InventoryAssemblerOutput output = new InventoryAssemblerOutput(player, assembler);
		InventorySocketable bullets = new InventorySocketable(assembler.getSocketableStack());

		addSlot(new SlotCADOutput(output, assembler, 120, 35));

		cadComponentStart = inventorySlots.size();
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.ASSEMBLY), 120, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.CORE), 100, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.SOCKET), 140, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.BATTERY), 110, 111));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.DYE), 130, 111));

		socketableStart = inventorySlots.size();
		addSlot(new SlotSocketable(assemblerInv, bullets, 0, 35, 21));
		socketableEnd = inventorySlots.size();

		bulletStart = inventorySlots.size();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 3; col++) {
				addSlot(new ValidatorSlot(bullets, col + row * 3, 17 + col * 18, 57 + row * 18));
			}
		}
		bulletEnd = inventorySlots.size();

		int xs = 48;
		int ys = 143;

		playerStart = inventorySlots.size();
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInventory, col + row * 9 + 9, xs + col * 18, ys + row * 18));
			}
		}
		playerEnd = inventorySlots.size();

		hotbarStart = inventorySlots.size();
		for (int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInventory, col, xs + col * 18, ys + 58));
		}
		hotbarEnd = inventorySlots.size();

		armorStart = inventorySlots.size();
		for (int armorSlot = 0; armorSlot < 4; armorSlot++) {
			final EquipmentSlotType slot = equipmentSlots[armorSlot];

			addSlot(new Slot(playerInventory, playerSize - 2 - armorSlot,
					xs - 27, ys + 18 * armorSlot) {
				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					return !stack.isEmpty() && stack.getItem().canEquip(stack, slot, player);
				}

				@OnlyIn(Dist.CLIENT)
				@Override
				public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
					return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, PlayerContainer.ARMOR_SLOT_TEXTURES[slot.getIndex()]);
				}
			});
		}

		addSlot(new Slot(playerInventory, playerSize - 1, 219, 143) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getBackgroundSprite() {
				return Pair.of(PlayerContainer.BLOCK_ATLAS_TEXTURE, PlayerContainer.EMPTY_OFFHAND_ARMOR_SLOT);
			}
		});
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
		return assembler.getPos().distanceSq(playerIn.getX(), playerIn.getY(), playerIn.getZ(), true) <= 64;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int from) {
		ItemStack mergeStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(from);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			mergeStack = stackInSlot.copy();

			if (from >= playerStart) {
				if (stackInSlot.getItem() instanceof ICADComponent) {
					EnumCADComponent componentType = ((ICADComponent) stackInSlot.getItem()).getComponentType(stackInSlot);
					int componentSlot = cadComponentStart + componentType.ordinal();
					if (!mergeItemStack(stackInSlot, componentSlot, componentSlot + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (ISocketable.isSocketable(stackInSlot)) {
					if (!mergeItemStack(stackInSlot, socketableStart, socketableEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if (ISpellAcceptor.isAcceptor(stackInSlot)) {
					if (!mergeItemStack(stackInSlot, bulletStart, bulletEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if (from < hotbarStart) {
					if (!mergeItemStack(stackInSlot, hotbarStart, hotbarEnd, true)) {
						return ItemStack.EMPTY;
					}
				} else if (!mergeItemStack(stackInSlot, playerStart, playerEnd, false)) {
					return ItemStack.EMPTY;
				}
			} else if (stackInSlot.getItem() instanceof ArmorItem) {
				ArmorItem armor = (ArmorItem) stackInSlot.getItem();
				int armorSlot = armorStart + armor.getEquipmentSlot().getSlotIndex() - 1;
				if (!mergeItemStack(stackInSlot, armorSlot, armorSlot + 1, true) &&
						!mergeItemStack(stackInSlot, playerStart, hotbarEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(stackInSlot, playerStart, hotbarEnd, true)) {
				return ItemStack.EMPTY;
			}

			slot.onSlotChanged();

			if (stackInSlot.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else if (stackInSlot.getCount() == mergeStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stackInSlot);
		}

		return mergeStack;
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		assembler.clearCachedCAD();
	}
}
