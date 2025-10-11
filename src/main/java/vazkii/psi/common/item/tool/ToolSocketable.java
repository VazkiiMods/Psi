/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.ComponentItemHandler;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModDataComponents;

import java.util.ArrayList;
import java.util.List;

public class ToolSocketable implements ICapabilityProvider<ItemCapability<?, Void>, Void, ToolSocketable>, ISocketable, IPsiBarDisplay, ISpellAcceptor {
	protected final ItemStack tool;
	protected final int slots;
	private final ComponentItemHandler toolHandler;

	public ToolSocketable(ItemStack tool, int slots) {
		this.tool = tool;
		this.slots = Mth.clamp(slots, 1, MAX_ASSEMBLER_SLOTS - 1);
		this.toolHandler = (ComponentItemHandler) tool.getCapability(Capabilities.ItemHandler.ITEM);
	}

	@Override
	public ToolSocketable getCapability(@NotNull ItemCapability<?, Void> capability, Void facing) {
		if(capability == PsiAPI.SOCKETABLE_CAPABILITY
				|| capability == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY
				|| capability == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) {
			return this;
		}
		return null;
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

		return toolHandler.getStackInSlot(slot);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		if(isSocketSlotAvailable(slot)) {
			toolHandler.setStackInSlot(slot, bullet);
		}
	}

	@Override
	public int getSelectedSlot() {
		return tool.getOrDefault(ModDataComponents.SELECTED_SLOT, 0);
	}

	@Override
	public void setSelectedSlot(int slot) {
		tool.set(ModDataComponents.SELECTED_SLOT, slot);
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
