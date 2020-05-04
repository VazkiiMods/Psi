/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.inventory.InventorySocketable;

import javax.annotation.Nonnull;

public class SlotSocketable extends SlotItemHandler {

	private final InventorySocketable bullets;

	public SlotSocketable(IItemHandlerModifiable inventoryIn, InventorySocketable bullets, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.bullets = bullets;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return ISocketableCapability.isSocketable(stack);
	}

	@Override
	public void onSlotChanged() {
		bullets.setStack(getStack());
	}
}
