/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/01/2019, 21:51:23 (GMT)]
 */
package vazkii.psi.api.inventory;

import com.google.common.collect.Iterators;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import vazkii.psi.api.cad.ISocketableCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;

public class InventorySocketable implements IInventory {

    @Nullable
    private ISocketableCapability socketable;

    public InventorySocketable(ItemStack stack) {
        if (stack.isEmpty())
            socketable = null;
        else
            socketable = ISocketableCapability.socketable(stack);
    }

    public void setStack(ItemStack stack) {
        if (stack.isEmpty())
            socketable = null;
        else
            socketable = ISocketableCapability.socketable(stack);
    }

    private Iterator<ItemStack> getSockerator() {
        if (socketable == null)
            return Collections.emptyIterator();
        return new IteratorSocketable(socketable);
    }

    @Override
    public int getSizeInventory() {
        Iterator<ItemStack> sockerator = getSockerator();
        return Iterators.size(sockerator);
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> sockerator = getSockerator();
        while (sockerator.hasNext())
            if (!sockerator.next().isEmpty())
                return false;
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        if (socketable == null)
            return ItemStack.EMPTY;
        return socketable.getBulletInSocket(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (socketable == null)
            return ItemStack.EMPTY;

        ItemStack bullet = socketable.getBulletInSocket(index);
        if (!bullet.isEmpty()) socketable.setBulletInSocket(index, ItemStack.EMPTY);
        return bullet;
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, 1);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack bullet) {
        if (socketable == null)
            return;

        socketable.setBulletInSocket(index, bullet);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        // NO-OP
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        // NO-OP
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        // NO-OP
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return socketable != null && socketable.isItemValid(index, stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        // NO-OP
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        Iterator<ItemStack> sockerator = getSockerator();
        while (sockerator.hasNext()) {
            sockerator.next();
            sockerator.remove();
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "psi.container.socketable";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }
}
