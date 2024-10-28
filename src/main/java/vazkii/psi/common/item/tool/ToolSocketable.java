/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.ComponentItemHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolSocketable implements ICapabilityProvider<ItemCapability<?, Void>, Void, ToolSocketable>, ISocketable, IPsiBarDisplay, ISpellAcceptor {
    protected final ItemStack tool;
    private final ComponentItemHandler toolHandler;;
    protected final int slots;

    public ToolSocketable(ItemStack tool, int slots) {
        this.tool = tool;
        this.slots = Mth.clamp(slots, 1, MAX_ASSEMBLER_SLOTS - 1);
        this.toolHandler = new ComponentItemHandler(this.tool, ModItems.TAG_BULLETS.get(), this.slots);
    }

    @Override
    public ToolSocketable getCapability(ItemCapability<?, Void> capability, Void facing) {
        if (capability == PsiAPI.SOCKETABLE_CAPABILITY
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
        for (int i = 0; i <= slots; i++) {
            list.add(i);
        }
        return list;
    }

    @Override
    public ItemStack getBulletInSocket(int slot) {
        return toolHandler.getStackInSlot(slot);
    }

    @Override
    public void setBulletInSocket(int slot, ItemStack bullet) {
        toolHandler.setStackInSlot(slot, bullet);
    }

    @Override
    public int getSelectedSlot() {
        return tool.getOrDefault(ModItems.TAG_SELECTED_SLOT, 0);
    }

    @Override
    public void setSelectedSlot(int slot) {
        tool.set(ModItems.TAG_SELECTED_SLOT, slot);
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
