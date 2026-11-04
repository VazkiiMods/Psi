/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.cad.*;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.event.PsiEvents;
import vazkii.psi.common.block.base.ModCADAssemblerBlock;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.LegacyItemStacks;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileCADAssembler extends BlockEntity implements ITileCADAssembler, MenuProvider, Container {
	private final CADStackHandler inventory = new CADStackHandler();
	private ItemStack cachedCAD = null;

	public TileCADAssembler(BlockPos pos, BlockState state) {
		super(ModCADAssemblerBlock.TYPE.get(), pos, state);
	}

	public Container getInventory() {
		return inventory;
	}

	@Override
	public void clearCachedCAD() {
		cachedCAD = null;
	}

	@Override
	public ItemStack getCachedCAD(Player player) {
		ItemStack cad = cachedCAD;
		if(cad == null) {
			ItemStack assembly = getStackForComponent(EnumCADComponent.ASSEMBLY);
			if(!assembly.isEmpty()) {
				List<ItemStack> components = IntStream.range(1, 6).mapToObj(inventory::getItem).collect(Collectors.toList());
				cad = ItemCAD.makeCADWithAssembly(player.level().registryAccess(), assembly, components);
			} else {
				cad = ItemStack.EMPTY;
			}

			AssembleCADEvent assembling = new AssembleCADEvent(cad, this, player);

			PsiEvents.post(assembling);

			if(assembling.isCanceled()) {
				cad = ItemStack.EMPTY;
			} else {
				cad = assembling.getCad();
			}

			cachedCAD = cad;
		}

		return cad;
	}

	@Override
	public ItemStack getStackForComponent(EnumCADComponent componentType) {
		return inventory.getItem(componentType.ordinal() + 1);
	}

	@Override
	public boolean setStackForComponent(EnumCADComponent componentType, ItemStack component) {
		int slot = componentType.ordinal() + 1;
		if(component.isEmpty() || CADComponentLookup.isComponent(registries(), component, componentType)) {
			inventory.setItem(slot, component);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getSocketableStack() {
		return inventory.getItem(0);
	}

	@Override
	public ISocketable getSocketable() {
		return ISocketable.socketable(getSocketableStack());
	}

	@Override
	public boolean setSocketableStack(ItemStack stack) {
		if(stack.isEmpty() || ISocketable.isSocketable(stack)) {
			inventory.setItem(0, stack);
			return true;
		}

		return false;
	}

	@Override
	public void onCraftCAD(ItemStack cad) {
		PsiEvents.post(new PostCADCraftEvent(cad, this));
		for(int i = 1; i < 6; i++) {
			inventory.setItem(i, ItemStack.EMPTY);
		}

		if(level == null) {
			return;
		}

		if(!level.isClientSide) {
			level.playSound(null, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, PsiSoundHandler.cadCreate.get(), SoundSource.BLOCKS, 0.5F, 1F);
		}
	}

	@Override
	public boolean isBulletSlotEnabled(int slot) {
		if(getSocketableStack().isEmpty()) {
			return false;
		}
		ISocketable socketable = getSocketable();
		return socketable != null && socketable.isSocketSlotAvailable(slot);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
		super.saveAdditional(tag, provider);
		ContainerHelper.saveAllItems(tag, inventory.getItems(), provider);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag cmp, HolderLookup.@NotNull Provider provider) {
		super.loadAdditional(cmp, provider);
		readPacketNBT(cmp, provider);
	}

	public void readPacketNBT(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
		// Migrate old CAD assemblers to the new format
		ListTag items = tag.getList("Items", 10);
		if(items.size() == 19) {
			for(int i = 0; i < inventory.getContainerSize(); i++) {
				inventory.setItem(i, ItemStack.EMPTY);
			}

			ISocketable socketable = null;

			for(int i = 0; i < items.size(); ++i) {
				if(i == 0) // Skip the fake CAD slot
				{
					continue;
				}

				ItemStack stack = LegacyItemStacks.parse(provider, items.getCompound(i));

				if(i == 6) { // Socketable item
					setSocketableStack(stack);

					if(!stack.isEmpty()) {
						socketable = PsiCapabilities.socketable(stack);
					}
				} else if(i == 1) // CORE
				{
					setStackForComponent(EnumCADComponent.CORE, stack);
				} else if(i == 2) // ASSEMBLY
				{
					setStackForComponent(EnumCADComponent.ASSEMBLY, stack);
				} else if(i == 3) // SOCKET
				{
					setStackForComponent(EnumCADComponent.SOCKET, stack);
				} else if(i == 4) // BATTERY
				{
					setStackForComponent(EnumCADComponent.BATTERY, stack);
				} else if(i == 5) // DYE
				{
					setStackForComponent(EnumCADComponent.DYE, stack);
				} else { // If we've gotten here, the item is a bullet.
					int idx = i - 7;
					if(socketable != null)
						socketable.setBulletInSocket(idx, stack);
				}
			}
		} else {
			for(int i = 0; i < items.size(); i++) {
				CompoundTag compoundtag = items.getCompound(i);
				int j = compoundtag.getByte("Slot") & 255;
				if(j < inventory.getItems().size()) {
					inventory.getItems().set(j, ItemStack.parse(provider, compoundtag).orElse(ItemStack.EMPTY));
				}
			}
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this, (BlockEntity e, RegistryAccess provider) -> getUpdateTag(provider));
	}

	@NotNull
	@Override
	public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
		CompoundTag cmp = new CompoundTag();
		saveAdditional(cmp, provider);
		return cmp;
	}

	@NotNull
	@Override
	public Component getDisplayName() {
		return Component.translatable(ModCADAssemblerBlock.BLOCK.get().getDescriptionId());
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
		return new ContainerCADAssembler(i, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return inventory.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return inventory.getItem(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return inventory.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return inventory.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.setItem(slot, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return inventory.canPlaceItem(slot, stack);
	}

	@Override
	public void clearContent() {
		inventory.clearContent();
	}

	@Nullable
	private HolderLookup.Provider registries() {
		return level == null ? null : level.registryAccess();
	}

	private class CADStackHandler extends SimpleContainer {

		private CADStackHandler() {
			super(6);
		}

		@Override
		public void setChanged() {
			super.setChanged();
			clearCachedCAD();
			TileCADAssembler.this.setChanged();
		}

		@Override
		public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
			if(stack.isEmpty()) {
				return true;
			}

			if(slot == 0) {
				return ISocketable.isSocketable(stack);
			} else if(slot < 6) {
				return CADComponentLookup.isComponent(registries(), stack, EnumCADComponent.values()[slot - 1]);
			}

			return false;
		}
	}
}
