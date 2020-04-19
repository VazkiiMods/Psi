/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.inventory;

import net.minecraft.item.ItemStack;

import vazkii.psi.api.cad.ISocketableCapability;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<ItemStack> {

	private final ISocketableCapability socketable;
	private int index = -1;
	private boolean removed = false;

	public IteratorSocketable(ISocketableCapability socketable) {
		this.socketable = socketable;
	}

	@Override
	public boolean hasNext() {
		return socketable.isSocketSlotAvailable(index + 1);
	}

	@Override
	public ItemStack next() {
		removed = false;
		return socketable.getBulletInSocket(index++);
	}

	@Override
	public void remove() {
		if (index < 0 || removed) {
			throw new IllegalStateException();
		}

		removed = true;
		socketable.setBulletInSocket(index, ItemStack.EMPTY);
	}
}
