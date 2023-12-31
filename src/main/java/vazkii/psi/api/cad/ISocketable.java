/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.lib.LibResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This capability defines items that can have Spell Bullets inserted into them.
 */
public interface ISocketable {

	List<ResourceLocation> signs = Arrays.asList(
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 0)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 1)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 2)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 3)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 4)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 5)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 6)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 7)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 8)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 9)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 10)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 11)),
			new ResourceLocation(String.format(LibResources.GUI_SIGN, 12))
	);

	int MAX_ASSEMBLER_SLOTS = 12;

	static Component getSocketedItemName(ItemStack stack, String fallbackKey) {
		if(stack.isEmpty() || !isSocketable(stack)) {
			return new TranslatableComponent(fallbackKey);
		}

		ISocketable socketable = socketable(stack);
		ItemStack item = socketable.getSelectedBullet();
		if(item.isEmpty()) {
			return new TranslatableComponent(fallbackKey);
		}

		return item.getHoverName();
	}

	static boolean isSocketable(ItemStack stack) {
		return !stack.isEmpty() && stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).isPresent();
	}

	static ISocketable socketable(ItemStack stack) {
		return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);
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
