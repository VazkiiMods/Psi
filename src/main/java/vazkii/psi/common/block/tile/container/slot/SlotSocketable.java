/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.inventory.InventorySocketable;

public class SlotSocketable extends Slot {

	private final InventorySocketable bullets;

	public SlotSocketable(IInventory inventoryIn, InventorySocketable bullets, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.bullets = bullets;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return ISocketableCapability.isSocketable(stack);
	}

	@Override
	public void onSlotChanged() {
		bullets.setStack(getStack());
	}
}
