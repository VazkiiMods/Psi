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

import vazkii.psi.api.PsiAPI;

public interface ISocketableCapability {

	static boolean isSocketable(ItemStack stack) {
		return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).isPresent();
	}

	static ISocketableCapability socketable(ItemStack stack) {
		return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);
	}

	boolean isSocketSlotAvailable(int slot);

	default boolean showSlotInRadialMenu(int slot) {
		return isSocketSlotAvailable(slot);
	}

	ItemStack getBulletInSocket(int slot);

	void setBulletInSocket(int slot, ItemStack bullet);

	int getSelectedSlot();

	void setSelectedSlot(int slot);

	boolean isItemValid(int slot, ItemStack bullet);

	boolean canLoopcast(ItemStack stack);
}
