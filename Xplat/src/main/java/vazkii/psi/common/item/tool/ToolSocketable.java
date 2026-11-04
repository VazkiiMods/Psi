/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModDataComponents;

import java.util.ArrayList;
import java.util.List;

public class ToolSocketable implements ISocketable, IPsiBarDisplay, ISpellAcceptor {
	protected final ItemStack tool;
	protected final int slots;

	public ToolSocketable(ItemStack tool, int slots) {
		this.tool = tool;
		this.slots = Mth.clamp(slots, 1, MAX_ASSEMBLER_SLOTS - 1);
	}

	@Override
	public boolean isSocketSlotAvailable(int slot) {
		return slot < slots && slot >= 0;
	}

	@Override
	public List<Integer> getRadialMenuSlots() {
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i <= slots; i++) {
			list.add(i);
		}
		return list;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		if(!isSocketSlotAvailable(slot)) {
			return ItemStack.EMPTY;
		}

		return bullets().get(slot);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		if(isSocketSlotAvailable(slot)) {
			NonNullList<ItemStack> bullets = bullets();
			bullets.set(slot, bullet.copy());
			tool.set(ModDataComponents.BULLETS.get(), ItemContainerContents.fromItems(bullets));
		}
	}

	private NonNullList<ItemStack> bullets() {
		NonNullList<ItemStack> bullets = NonNullList.withSize(slots, ItemStack.EMPTY);
		tool.getOrDefault(ModDataComponents.BULLETS.get(), ItemContainerContents.EMPTY).copyInto(bullets);
		return bullets;
	}

	@Override
	public int getSelectedSlot() {
		return tool.getOrDefault(ModDataComponents.SELECTED_SLOT.get(), 0);
	}

	@Override
	public void setSelectedSlot(int slot) {
		tool.set(ModDataComponents.SELECTED_SLOT.get(), slot);
	}

	@Override
	public int getLastSlot() {
		return slots - 1;
	}

	@Override
	public boolean shouldShow(IPlayerData data) {
		return false;
	}

	@Override
	public void setSpell(Player player, Spell spell) {
		int slot = getSelectedSlot();
		ItemStack bullet = getBulletInSocket(slot);
		if(!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(slot, bullet);
		}
	}

}
