/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.inventory;

import com.google.common.collect.Iterators;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.ISocketable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Iterator;

public class InventorySocketable implements Container, Nameable, ContainerData {

	@Nullable
	private ISocketable socketable;

	public InventorySocketable(ItemStack stack) {
		if (stack.isEmpty()) {
			socketable = null;
		} else {
			socketable = ISocketable.socketable(stack);
		}
	}

	public void setStack(ItemStack stack) {
		if (stack.isEmpty()) {
			socketable = null;
		} else {
			socketable = ISocketable.socketable(stack);
		}
	}

	private Iterator<ItemStack> getSockerator() {
		if (socketable == null) {
			return Collections.emptyIterator();
		}
		return new IteratorSocketable(socketable);
	}

	@Override
	public int getContainerSize() {
		Iterator<ItemStack> sockerator = getSockerator();
		return Iterators.size(sockerator);
	}

	@Override
	public boolean isEmpty() {
		Iterator<ItemStack> sockerator = getSockerator();
		while (sockerator.hasNext()) {
			if (!sockerator.next().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getItem(int index) {
		if (socketable == null) {
			return ItemStack.EMPTY;
		}
		return socketable.getBulletInSocket(index);
	}

	@Nonnull
	@Override
	public ItemStack removeItem(int index, int count) {
		if (socketable == null) {
			return ItemStack.EMPTY;
		}

		ItemStack bullet = socketable.getBulletInSocket(index);
		if (!bullet.isEmpty()) {
			socketable.setBulletInSocket(index, ItemStack.EMPTY);
		}
		return bullet;
	}

	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return removeItem(index, 1);
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack bullet) {
		if (socketable == null) {
			return;
		}

		socketable.setBulletInSocket(index, bullet);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void setChanged() {
		// NO-OP
	}

	@Override
	public boolean stillValid(@Nonnull Player player) {
		return true;
	}

	@Override
	public void startOpen(@Nonnull Player player) {
		// NO-OP
	}

	@Override
	public void stopOpen(@Nonnull Player player) {
		// NO-OP
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return socketable != null && socketable.isItemValid(index, stack);
	}

	@Override
	public int get(int id) {
		return 0;
	}

	@Override
	public void set(int id, int value) {
		// NO-OP
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public void clearContent() {
		Iterator<ItemStack> sockerator = getSockerator();
		while (sockerator.hasNext()) {
			sockerator.next();
			sockerator.remove();
		}
	}

	@Nonnull
	@Override
	public Component getName() {
		return Component.translatable("psi.container.socketable");
	}

}
