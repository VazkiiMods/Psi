/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;

/**
 * This interface defines an item that can have Spell Bullets
 * put into it.
 *
 * As of version 73, this interface should not be used directly,
 * instead interacting with the item via its {@link ISocketableCapability}.
 */
public interface ISocketable extends IShowPsiBar {

	int MAX_SLOTS = 12;

	static ITextComponent getSocketedItemName(ItemStack stack, String fallbackKey) {
		if (stack.isEmpty() || !ISocketableCapability.isSocketable(stack)) {
			return new TranslationTextComponent(fallbackKey);
		}

		ISocketableCapability socketable = ISocketableCapability.socketable(stack);
		ItemStack item = socketable.getBulletInSocket(socketable.getSelectedSlot());
		if (item.isEmpty()) {
			return new TranslationTextComponent(fallbackKey);
		}

		return item.getDisplayName();
	}

	/**
	 * Gets if the passed in slot is available for inserting bullets given the ItemStack passed in.
	 */
	boolean isSocketSlotAvailable(ItemStack stack, int slot);

	/**
	 * Gets whether the passed in slot should be shown in the radial menu.
	 */
	default boolean showSlotInRadialMenu(ItemStack stack, int slot) {
		return isSocketSlotAvailable(stack, slot);
	}

	/**
	 * Gets the bullet in the slot passed in. Can be null.
	 */
	ItemStack getBulletInSocket(ItemStack stack, int slot);

	/**
	 * Sets the bullet in the slot passed in.
	 */
	void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet);

	/**
	 * Gets the slot that is currently selected.
	 */
	int getSelectedSlot(ItemStack stack);

	/**
	 * Sets ths currently selected slot.
	 */
	void setSelectedSlot(ItemStack stack, int slot);

	default boolean isItemValid(ItemStack stack, int slot, ItemStack bullet) {
		if (!isSocketSlotAvailable(stack, slot)) {
			return false;
		}

		if (bullet.isEmpty() || !ISpellAcceptor.hasSpell(bullet)) {
			return false;
		}

		ISpellAcceptor container = ISpellAcceptor.acceptor(bullet);

		return stack.getItem() instanceof ICAD || !container.isCADOnlyContainer();
	}

	default boolean canLoopcast(ItemStack stack) {
		return stack.getItem() instanceof ICAD;
	}

	@Override
	default boolean shouldShow(ItemStack stack, IPlayerData data) {
		return true;
	}
}
