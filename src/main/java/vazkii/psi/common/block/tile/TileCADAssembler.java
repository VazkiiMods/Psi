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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class TileCADAssembler extends TileSimpleInventory implements ITileCADAssembler {

	private transient ItemStack cachedCAD;

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
	public ItemStack getCachedCAD(EntityPlayer player) {
		ItemStack cad = cachedCAD;
		if (cad == null) {
			if (!getStackForComponent(EnumCADComponent.ASSEMBLY).isEmpty())
				cad = ItemCAD.makeCAD(inventorySlots.subList(1, 6));
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
	public ItemStack getSocketableStack() {
		return getStackInSlot(0);
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
		ItemStack socketableStack = getSocketableStack();
		if(!socketableStack.isEmpty() && socketableStack.getItem() instanceof ISocketable) {
			ISocketable socketable = (ISocketable) socketableStack.getItem();
			return socketable.isSocketSlotAvailable(socketableStack, slot);
		}

		return false;
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Nonnull
	@Override
	public String getName() {
		return LibBlockNames.CAD_ASSEMBLER;
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
			return stack.getItem() instanceof ISocketable;
		else if (slot < 6)
			return stack.getItem() instanceof ICADComponent &&
				((ICADComponent) stack.getItem()).getComponentType(stack) == EnumCADComponent.values()[slot - 1];

		return false;
	}
}
