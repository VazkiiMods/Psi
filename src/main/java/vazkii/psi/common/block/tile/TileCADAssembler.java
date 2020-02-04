/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 15:52:20 (GMT)]
 */
package vazkii.psi.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibBlockNames;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCADAssembler extends TileSimpleInventory implements ITileCADAssembler, INamedContainerProvider {
	@ObjectHolder(LibMisc.PREFIX_MOD + LibBlockNames.CAD_ASSEMBLER)
	public static TileEntityType<TileCADAssembler> TYPE;

	private transient ItemStack cachedCAD;

	public TileCADAssembler() {
		super(TYPE);
	}

	@Override
	public void inventoryChanged(int i) {
		if (0 < i && i < 6)
			clearCachedCAD();
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
			if (!assembly.isEmpty())
				cad = ItemCAD.makeCADWithAssembly(assembly, inventorySlots.subList(1, 6));
			else
				cad = ItemStack.EMPTY;

			AssembleCADEvent assembling = new AssembleCADEvent(cad, this, player);

			MinecraftForge.EVENT_BUS.post(assembling);

			if (assembling.isCanceled())
				cad = ItemStack.EMPTY;
			else
				cad = assembling.getCad();

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
		return getStackInSlot(getComponentSlot(componentType));
	}

	@Override
	public boolean setStackForComponent(EnumCADComponent componentType, ItemStack component) {
		int slot = getComponentSlot(componentType);
		if (component.isEmpty()) {
			setInventorySlotContents(slot, component);
			return true;
		} else if (component.getItem() instanceof ICADComponent) {
			ICADComponent componentItem = (ICADComponent) component.getItem();
			if (componentItem.getComponentType(component) == componentType) {
				setInventorySlotContents(slot, component);
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack getSocketableStack() {
		return getStackInSlot(0);
	}

	@Override
	public ISocketableCapability getSocketable() {
		return ISocketableCapability.socketable(getSocketableStack());
	}

	@Override
	public boolean setSocketableStack(ItemStack stack) {
		if (stack.isEmpty() || ISocketableCapability.isSocketable(stack)) {
			setInventorySlotContents(0, stack);
			return true;
		}

		return false;
	}

	@Override
	public void onCraftCAD(ItemStack cad) {
		MinecraftForge.EVENT_BUS.post(new PostCADCraftEvent(cad, this));
		for (int i = 1; i < 6; i++)
			setInventorySlotContents(i, ItemStack.EMPTY);
		if (!world.isRemote)
			world.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, PsiSoundHandler.cadCreate, SoundCategory.BLOCKS, 0.5F, 1F);
	}

	@Override
	public boolean isBulletSlotEnabled(int slot) {
		ISocketableCapability socketable = getSocketable();
		return socketable != null && socketable.isSocketSlotAvailable(slot);
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Override
	public boolean isAutomationEnabled() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
		if (stack.isEmpty())
			return true;

		if (slot == 0)
			return ISocketableCapability.isSocketable(stack);
		else if (slot < 6)
			return stack.getItem() instanceof ICADComponent &&
				((ICADComponent) stack.getItem()).getComponentType(stack) == EnumCADComponent.values()[slot - 1];

		return false;
	}

	@Override
	public void writeSharedNBT(CompoundNBT tag) {
		super.writeSharedNBT(tag);
		tag.putInt("version", 1);
	}

	@Override
	public void readSharedNBT(CompoundNBT tag) {
		// Migrate old CAD assemblers to the new format
		if (needsToSyncInventory() && tag.getInt("version") < 1) {
			ListNBT items = tag.getList("Items", 10);
			this.clear();

			LazyOptional<ISocketableCapability> socketable = LazyOptional.empty();

			for(int i = 0; i < items.size(); ++i) {
				if (i == 0) // Skip the fake CAD slot
					continue;

				ItemStack stack = ItemStack.read(items.getCompound(i));

				if (i == 6) { // Socketable item
					setSocketableStack(stack);

					if (!stack.isEmpty())
						socketable = stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY);
				} else if (i == 1) // CORE
					setStackForComponent(EnumCADComponent.CORE, stack);
				else if (i == 2) // ASSEMBLY
					setStackForComponent(EnumCADComponent.ASSEMBLY, stack);
				else if (i == 3) // SOCKET
					setStackForComponent(EnumCADComponent.SOCKET, stack);
				else if (i == 4) // BATTERY
					setStackForComponent(EnumCADComponent.BATTERY, stack);
				else if (i == 5) // DYE
					setStackForComponent(EnumCADComponent.DYE, stack);
				else { // If we've gotten here, the item is a bullet.
					int idx = i - 7;
					socketable.ifPresent(s -> s.setBulletInSocket(idx, stack));
				}
			}
		} else
			super.readSharedNBT(tag);
	}

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
