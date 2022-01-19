/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToolSocketable implements ICapabilityProvider, ISocketable, IPsiBarDisplay, ISpellAcceptor {
	protected final ItemStack tool;
	protected final int slots;

	private final LazyOptional<?> capOptional;

	public ToolSocketable(ItemStack tool, int slots) {
		this.tool = tool;
		this.slots = MathHelper.clamp(slots, 1, MAX_ASSEMBLER_SLOTS - 1);
		this.capOptional = LazyOptional.of(() -> this);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == PsiAPI.SOCKETABLE_CAPABILITY
				|| cap == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY
				|| cap == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) {
			return capOptional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public boolean isSocketSlotAvailable(int slot) {
		return slot < slots && slot >= 0;
	}

	@Override
	public List<Integer> getRadialMenuSlots() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i <= slots; i++) {
			list.add(i);
		}
		return list;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		String name = IPsimetalTool.TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = tool.getOrCreateTag().getCompound(name);

		if (cmp.isEmpty()) {
			return ItemStack.EMPTY;
		}

		return ItemStack.of(cmp);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		String name = IPsimetalTool.TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = new CompoundNBT();

		if (!bullet.isEmpty()) {
			cmp = bullet.save(cmp);
		}

		tool.getOrCreateTag().put(name, cmp);
	}

	@Override
	public int getSelectedSlot() {
		return tool.getOrCreateTag().getInt(IPsimetalTool.TAG_SELECTED_SLOT);
	}

	@Override
	public void setSelectedSlot(int slot) {
		tool.getOrCreateTag().putInt(IPsimetalTool.TAG_SELECTED_SLOT, slot);
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
	public void setSpell(PlayerEntity player, Spell spell) {
		int slot = getSelectedSlot();
		ItemStack bullet = getBulletInSocket(slot);
		if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(slot, bullet);
		}
	}

	@Override
	public boolean castableFromSocket() {
		return false;
	}
}
