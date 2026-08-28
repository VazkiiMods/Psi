/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.inventory;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.ISocketable;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<ItemStack> {

	private final ISocketable socketable;
	private int index = -1;
	private boolean removed = false;

	public IteratorSocketable(ISocketable socketable) {
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
		if(index < 0 || removed) {
			throw new IllegalStateException();
		}

		removed = true;
		socketable.setBulletInSocket(index, ItemStack.EMPTY);
	}
}
