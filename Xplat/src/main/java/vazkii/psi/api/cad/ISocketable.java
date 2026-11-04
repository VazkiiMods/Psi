/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.capability.PsiCapabilities;
import vazkii.psi.api.spell.ISpellAcceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This capability defines items that can have Spell Bullets inserted into them.
 */
public interface ISocketable {

	List<ResourceLocation> signs = Arrays.asList(
			PsiAPI.location("textures/gui/signs/sign0.png"),
			PsiAPI.location("textures/gui/signs/sign1.png"),
			PsiAPI.location("textures/gui/signs/sign2.png"),
			PsiAPI.location("textures/gui/signs/sign3.png"),
			PsiAPI.location("textures/gui/signs/sign4.png"),
			PsiAPI.location("textures/gui/signs/sign5.png"),
			PsiAPI.location("textures/gui/signs/sign6.png"),
			PsiAPI.location("textures/gui/signs/sign7.png"),
			PsiAPI.location("textures/gui/signs/sign8.png"),
			PsiAPI.location("textures/gui/signs/sign9.png"),
			PsiAPI.location("textures/gui/signs/sign10.png"),
			PsiAPI.location("textures/gui/signs/sign11.png"),
			PsiAPI.location("textures/gui/signs/sign12.png")
	);

	int MAX_ASSEMBLER_SLOTS = 12;

	static Component getSocketedItemName(ItemStack stack, String fallbackKey) {
		if(stack.isEmpty() || !isSocketable(stack)) {
			return Component.translatable(fallbackKey);
		}

		ISocketable socketable = socketable(stack);
		ItemStack item = socketable.getSelectedBullet();
		if(item.isEmpty()) {
			return Component.translatable(fallbackKey);
		}

		return item.getHoverName();
	}

	static boolean isSocketable(ItemStack stack) {
		return !stack.isEmpty() && PsiCapabilities.socketable(stack) != null;
	}

	static ISocketable socketable(ItemStack stack) {
		ISocketable capability = PsiCapabilities.socketable(stack);
		if(capability == null) {
			throw new NullPointerException();
		}
		return capability;
	}

	boolean isSocketSlotAvailable(int slot);

	default List<Integer> getRadialMenuSlots() {
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < MAX_ASSEMBLER_SLOTS; i++) {
			if(isSocketSlotAvailable(i)) {
				list.add(i);
			}
		}
		return list;
	}

	default List<ResourceLocation> getRadialMenuIcons() {
		return signs;
	}

	ItemStack getBulletInSocket(int slot);

	void setBulletInSocket(int slot, ItemStack bullet);

	int getSelectedSlot();

	void setSelectedSlot(int slot);

	default int getLastSlot() {
		int slot = 0;
		while(isSocketSlotAvailable(slot + 1)) {
			slot++;
		}
		return slot;
	}

	default ItemStack getSelectedBullet() {
		return getBulletInSocket(getSelectedSlot());
	}

	default boolean isItemValid(int slot, ItemStack bullet) {
		if(!isSocketSlotAvailable(slot)) {
			return false;
		}

		if(!ISpellAcceptor.isContainer(bullet)) {
			return false;
		}

		ISpellAcceptor container = ISpellAcceptor.acceptor(bullet);

		return this instanceof ICADData || !container.isCADOnlyContainer();
	}

	default boolean canLoopcast() {
		return this instanceof ICADData;
	}
}
