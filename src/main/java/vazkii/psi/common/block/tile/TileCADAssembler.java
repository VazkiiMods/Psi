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

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import vazkii.arl.block.tile.TileSimpleInventory;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibBlockNames;

public class TileCADAssembler extends TileSimpleInventory implements ITickable {

	boolean ignoreChanges = false;

	@Override
	public void inventoryChanged(int i) {
		if(!ignoreChanges) {
			if(i != 0) {
				ItemStack cad = null;
				if(getStackInSlot(2) != null)
					cad = ItemCAD.makeCAD(Arrays.copyOfRange(inventorySlots, 1, 6));

				setInventorySlotContents(0, cad);
			}

			ItemStack socketableStack = getStackInSlot(6);
			if(i == 6 && socketableStack != null && socketableStack.getItem() instanceof ISocketable) {
				ISocketable socketable = (ISocketable) socketableStack.getItem();

				for(int j = 0; j < 12; j++)
					if(socketable.isSocketSlotAvailable(socketableStack, j)) {
						ItemStack bullet = socketable.getBulletInSocket(socketableStack, j);
						setInventorySlotContents(j + 7, bullet);
					} else setInventorySlotContents(j + 7, null);
			}
		}
	}

	@Override
	public void update() {
		ItemStack socketableStack = getStackInSlot(6);
		if(socketableStack != null && socketableStack.getItem() instanceof ISocketable) {
			ISocketable socketable = (ISocketable) socketableStack.getItem();
			for(int j = 0; j < 12; j++)
				if(socketable.isSocketSlotAvailable(socketableStack, j)) {
					ItemStack bullet = getStackInSlot(j + 7);
					socketable.setBulletInSocket(socketableStack, j, bullet);
				}
		} else for(int j = 0; j < 12; j++)
			setInventorySlotContents(j + 7, null);
	}

	public void onCraftCAD() {
		ignoreChanges = true;
		for(int i = 1; i < 6; i++)
			setInventorySlotContents(i, null);
		if(!worldObj.isRemote)
			worldObj.playSound(null, getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5, PsiSoundHandler.cadCreate, SoundCategory.BLOCKS, 0.5F, 1F);
		ignoreChanges = false;
	}

	public boolean isBulletSlotEnabled(int slot) {
		ItemStack socketableStack = getStackInSlot(6);
		if(socketableStack != null && socketableStack.getItem() instanceof ISocketable) {
			ISocketable socketable = (ISocketable) socketableStack.getItem();
			return socketable.isSocketSlotAvailable(socketableStack, slot);
		}

		return false;
	}

	@Override
	public int getSizeInventory() {
		return 19;
	}

	@Override
	public String getName() {
		return LibBlockNames.CAD_ASSEMBLER;
	}

	@Override
	public boolean isAutomationEnabled() {
		return false;
	}

}
