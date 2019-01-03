/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 21:52:33 (GMT)]
 */
package vazkii.psi.api.inventory;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ISocketable;

import java.util.Iterator;

public class IteratorSocketable implements Iterator<ItemStack> {

    private final ItemStack stack;
    private final ISocketable socketable;
    private int index = -1;
    private boolean removed = false;

    public IteratorSocketable(ItemStack stack) {
        this.stack = stack;
        this.socketable = (ISocketable) stack.getItem();
    }

    @Override
    public boolean hasNext() {
        return socketable.isSocketSlotAvailable(stack, index + 1);
    }

    @Override
    public ItemStack next() {
        removed = false;
        return socketable.getBulletInSocket(stack, index++);
    }

    @Override
    public void remove() {
        if (index < 0 || removed)
            throw new IllegalStateException();

        removed = true;
        socketable.setBulletInSocket(stack, index, ItemStack.EMPTY);
    }
}
