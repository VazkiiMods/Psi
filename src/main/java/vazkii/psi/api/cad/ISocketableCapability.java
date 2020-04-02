/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 03, 2019, 15:16 AM (EST)]
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
