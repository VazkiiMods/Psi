/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.block.tile.container.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.inventory.InventorySocketable;

public class SlotSocketable extends SlotItemHandler {

	private final InventorySocketable bullets;

	public SlotSocketable(IItemHandlerModifiable inventoryIn, InventorySocketable bullets, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.bullets = bullets;
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return ISocketable.isSocketable(stack);
	}

	@Override
	public void setChanged() {
		bullets.setStack(getItem());
	}
}
