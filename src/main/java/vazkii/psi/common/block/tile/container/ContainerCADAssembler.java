/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
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

public class ContainerCADAssembler extends AbstractContainerMenu {
	@ObjectHolder(registryName = "minecraft:menu", value = LibMisc.PREFIX_MOD + LibBlockNames.CAD_ASSEMBLER)
	public static MenuType<ContainerCADAssembler> TYPE;

	private static final EquipmentSlot[] equipmentSlots = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

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

	public static ContainerCADAssembler fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		return new ContainerCADAssembler(windowId, playerInventory, (TileCADAssembler) playerInventory.player.level().getBlockEntity(pos));
	}

	public ContainerCADAssembler(int windowId, Inventory playerInventory, TileCADAssembler assembler) {
		super(TYPE, windowId);
		Player player = playerInventory.player;
		int playerSize = playerInventory.getContainerSize();

		this.assembler = assembler;
		IItemHandlerModifiable assemblerInv = assembler.getInventory();
		assembler.clearCachedCAD();

		InventoryAssemblerOutput output = new InventoryAssemblerOutput(player, assembler);
		InventorySocketable bullets = new InventorySocketable(assembler.getSocketableStack());

		addSlot(new SlotCADOutput(output, assembler, 120, 35));

		cadComponentStart = slots.size();
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.ASSEMBLY), 120, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.CORE), 100, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.SOCKET), 140, 91));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.BATTERY), 110, 111));
		addSlot(new SlotItemHandler(assemblerInv, assembler.getComponentSlot(EnumCADComponent.DYE), 130, 111));

		socketableStart = slots.size();
		addSlot(new SlotSocketable(assemblerInv, bullets, 0, 35, 21));
		socketableEnd = slots.size();

		bulletStart = slots.size();
		for(int row = 0; row < 4; row++) {
			for(int col = 0; col < 3; col++) {
				addSlot(new ValidatorSlot(bullets, col + row * 3, 17 + col * 18, 57 + row * 18));
			}
		}
		bulletEnd = slots.size();

		int xs = 48;
		int ys = 143;

		playerStart = slots.size();
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 9; col++) {
				addSlot(new Slot(playerInventory, col + row * 9 + 9, xs + col * 18, ys + row * 18));
			}
		}
		playerEnd = slots.size();

		hotbarStart = slots.size();
		for(int col = 0; col < 9; col++) {
			addSlot(new Slot(playerInventory, col, xs + col * 18, ys + 58));
		}
		hotbarEnd = slots.size();

		armorStart = slots.size();
		for(int armorSlot = 0; armorSlot < 4; armorSlot++) {
			final EquipmentSlot slot = equipmentSlots[armorSlot];

			addSlot(new Slot(playerInventory, playerSize - 2 - armorSlot,
					xs - 27, ys + 18 * armorSlot) {
				@Override
				public int getMaxStackSize() {
					return 1;
				}

				@Override
				public boolean mayPlace(ItemStack stack) {
					return !stack.isEmpty() && stack.getItem().canEquip(stack, slot, player);
				}

				@OnlyIn(Dist.CLIENT)
				@Override
				public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
					return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[slot.getIndex()]);
				}
			});
		}

		addSlot(new Slot(playerInventory, playerSize - 1, 219, 143) {
			@OnlyIn(Dist.CLIENT)
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
			}
		});
	}

	@Override
	public boolean stillValid(@Nonnull Player playerIn) {
		return !playerIn.isRemoved() && assembler.getBlockPos().distToCenterSqr(playerIn.position()) <= 64;
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(Player playerIn, int from) {
		ItemStack mergeStack = ItemStack.EMPTY;
		Slot slot = slots.get(from);

		if(slot != null && slot.hasItem()) {
			ItemStack stackInSlot = slot.getItem();
			mergeStack = stackInSlot.copy();

			if(from >= playerStart) {
				if(stackInSlot.getItem() instanceof ICADComponent) {
					EnumCADComponent componentType = ((ICADComponent) stackInSlot.getItem()).getComponentType(stackInSlot);
					int componentSlot = cadComponentStart + componentType.ordinal();
					if(!moveItemStackTo(stackInSlot, componentSlot, componentSlot + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if(ISocketable.isSocketable(stackInSlot)) {
					if(!moveItemStackTo(stackInSlot, socketableStart, socketableEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if(ISpellAcceptor.isContainer(stackInSlot)) {
					if(!moveItemStackTo(stackInSlot, bulletStart, bulletEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if(from < hotbarStart) {
					if(!moveItemStackTo(stackInSlot, hotbarStart, hotbarEnd, true)) {
						return ItemStack.EMPTY;
					}
				} else if(!moveItemStackTo(stackInSlot, playerStart, playerEnd, false)) {
					return ItemStack.EMPTY;
				}
			} else if(stackInSlot.getItem() instanceof ArmorItem) {
				ArmorItem armor = (ArmorItem) stackInSlot.getItem();
				int armorSlot = armorStart + armor.getType().ordinal() - 1;
				if(!moveItemStackTo(stackInSlot, armorSlot, armorSlot + 1, true) &&
						!moveItemStackTo(stackInSlot, playerStart, hotbarEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else if(!moveItemStackTo(stackInSlot, playerStart, hotbarEnd, true)) {
				return ItemStack.EMPTY;
			}

			slot.setChanged();

			if(stackInSlot.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else if(stackInSlot.getCount() == mergeStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stackInSlot);
		}

		return mergeStack;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		assembler.clearCachedCAD();
	}
}
