/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.AssembleCADEvent;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.cad.ITileCADAssembler;
import vazkii.psi.api.cad.PostCADCraftEvent;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileCADAssembler extends TileEntity implements ITileCADAssembler, INamedContainerProvider {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibBlockNames.CAD_ASSEMBLER)
	public static TileEntityType<TileCADAssembler> TYPE;

	private final IItemHandlerModifiable inventory = new ItemStackHandler(6) {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			if (0 < slot && slot < 6) {
				clearCachedCAD();
			}
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			if (stack.isEmpty()) {
				return true;
			}

			if (slot == 0) {
				return ISocketable.isSocketable(stack);
			} else if (slot < 6) {
				return stack.getItem() instanceof ICADComponent &&
						((ICADComponent) stack.getItem()).getComponentType(stack) == EnumCADComponent.values()[slot - 1];
			}

			return false;
		}
	};
	private final IItemHandler publicInv = new IItemHandler() {
		@Override
		public int getSlots() {
			return inventory.getSlots();
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return inventory.getStackInSlot(slot);
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return stack;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot);
		}

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return inventory.isItemValid(slot, stack);
		}
	};
	private ItemStack cachedCAD;

	public TileCADAssembler() {
		super(TYPE);
	}

	public IItemHandlerModifiable getInventory() {
		return inventory;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> publicInv));
	}

	@Override
	public void clearCachedCAD() {
		cachedCAD = null;
	}

	@Override
	public ItemStack getCachedCAD(PlayerEntity player) {
		ItemStack cad = cachedCAD;
		if (cad == null) {
			ItemStack assembly = getStackForComponent(EnumCADComponent.ASSEMBLY);
			if (!assembly.isEmpty()) {
				List<ItemStack> components = IntStream.range(1, 6).mapToObj(inventory::getStackInSlot).collect(Collectors.toList());
				cad = ItemCAD.makeCADWithAssembly(assembly, components);
			} else {
				cad = ItemStack.EMPTY;
			}

			AssembleCADEvent assembling = new AssembleCADEvent(cad, this, player);

			MinecraftForge.EVENT_BUS.post(assembling);

			if (assembling.isCanceled()) {
				cad = ItemStack.EMPTY;
			} else {
				cad = assembling.getCad();
			}

			cachedCAD = cad;
		}

		return cad;
	}

	@Override
	public int getComponentSlot(EnumCADComponent componentType) {
		return componentType.ordinal() + 1;
	}

	@Override
	public ItemStack getStackForComponent(EnumCADComponent componentType) {
		return inventory.getStackInSlot(getComponentSlot(componentType));
	}

	@Override
	public boolean setStackForComponent(EnumCADComponent componentType, ItemStack component) {
		int slot = getComponentSlot(componentType);
		if (component.isEmpty()) {
			inventory.setStackInSlot(slot, component);
			return true;
		} else if (component.getItem() instanceof ICADComponent) {
			ICADComponent componentItem = (ICADComponent) component.getItem();
			if (componentItem.getComponentType(component) == componentType) {
				inventory.setStackInSlot(slot, component);
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack getSocketableStack() {
		return inventory.getStackInSlot(0);
	}

	@Override
	public ISocketable getSocketable() {
		return ISocketable.socketable(getSocketableStack());
	}

	@Override
	public boolean setSocketableStack(ItemStack stack) {
		if (stack.isEmpty() || ISocketable.isSocketable(stack)) {
			inventory.setStackInSlot(0, stack);
			return true;
		}

		return false;
	}

	@Override
	public void onCraftCAD(ItemStack cad) {
		MinecraftForge.EVENT_BUS.post(new PostCADCraftEvent(cad, this));
		for (int i = 1; i < 6; i++) {
			inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
		if (!world.isRemote) {
			world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, PsiSoundHandler.cadCreate, SoundCategory.BLOCKS, 0.5F, 1F);
		}
	}

	@Override
	public boolean isBulletSlotEnabled(int slot) {
		if (getSocketableStack().isEmpty()) {
			return false;
		}
		ISocketable socketable = getSocketable();
		return socketable != null && socketable.isSocketSlotAvailable(slot);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT tag) {
		tag = super.write(tag);
		tag.putInt("version", 1);
		tag.put("Items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
		return tag;
	}

	@Override
	public void read(@Nonnull CompoundNBT tag) {
		super.read(tag);

		// Migrate old CAD assemblers to the new format
		if (tag.getInt("version") < 1) {
			ListNBT items = tag.getList("Items", 10);
			for (int i = 0; i < inventory.getSlots(); i++) {
				inventory.setStackInSlot(i, ItemStack.EMPTY);
			}

			LazyOptional<ISocketable> socketable = LazyOptional.empty();

			for (int i = 0; i < items.size(); ++i) {
				if (i == 0) // Skip the fake CAD slot
				{
					continue;
				}

				ItemStack stack = ItemStack.read(items.getCompound(i));

				if (i == 6) { // Socketable item
					setSocketableStack(stack);

					if (!stack.isEmpty()) {
						socketable = stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY);
					}
				} else if (i == 1) // CORE
				{
					setStackForComponent(EnumCADComponent.CORE, stack);
				} else if (i == 2) // ASSEMBLY
				{
					setStackForComponent(EnumCADComponent.ASSEMBLY, stack);
				} else if (i == 3) // SOCKET
				{
					setStackForComponent(EnumCADComponent.SOCKET, stack);
				} else if (i == 4) // BATTERY
				{
					setStackForComponent(EnumCADComponent.BATTERY, stack);
				} else if (i == 5) // DYE
				{
					setStackForComponent(EnumCADComponent.DYE, stack);
				} else { // If we've gotten here, the item is a bullet.
					int idx = i - 7;
					socketable.ifPresent(s -> s.setBulletInSocket(idx, stack));
				}
			}
		} else {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag.getList("Items", Constants.NBT.TAG_COMPOUND));
		}
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getPos(), -1, getUpdateTag());
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(ModBlocks.cadAssembler.getTranslationKey());
	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new ContainerCADAssembler(i, playerInventory, this);
	}
}
