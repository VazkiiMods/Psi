/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.ComponentItemHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADSocket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CADData implements ICapabilityProvider<ItemCapability<?, Void>, Void, CADData>, ICADData, ISpellAcceptor, ISocketable, IPsiBarDisplay {

    private final ItemStack cad;
    private final ComponentItemHandler cadHandler;
    private int time;
    private int battery;
    private List<Vector3> vectors = Lists.newArrayList();

    private boolean dirty;

    public CADData(ItemStack cad) {
        this.cad = cad;
        this.cadHandler = new ComponentItemHandler(this.cad, ModItems.TAG_BULLETS.get(), getLastSlot() + 1);
    }

    @Nullable
    @Override
    public CADData getCapability(@Nonnull ItemCapability<?, Void> capability, @Nullable Void facing) {
        if (capability == PsiAPI.SOCKETABLE_CAPABILITY
                || capability == PsiAPI.CAD_DATA_CAPABILITY
                || capability == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY
                || capability == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) {
            return this;
        }
        return null;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public void setTime(int time) {
        if (this.time != time) {
            this.time = time;
        }
    }

    @Override
    public int getBattery() {
        return battery;
    }

    @Override
    public void setBattery(int battery) {
        this.battery = battery;
    }

    @Override
    public Vector3 getSavedVector(int memorySlot) {
        if (vectors.size() <= memorySlot) {
            return Vector3.zero.copy();
        }

        Vector3 vec = vectors.get(memorySlot);
        return (vec == null ? Vector3.zero : vec).copy();
    }

    @Override
    public void setSavedVector(int memorySlot, Vector3 value) {
        while (vectors.size() <= memorySlot) {
            vectors.add(null);
        }

        vectors.set(memorySlot, value);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markDirty(boolean isDirty) {
        dirty = isDirty;
    }

    @Override
    public void setSpell(Player player, Spell spell) {
        int slot = getSelectedSlot();
        ItemStack bullet = getBulletInSocket(slot);
        if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
            ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
            setBulletInSocket(slot, bullet);
            player.getCooldowns().addCooldown(cad.getItem(), 10);
        }
    }

    @Override
    public boolean requiresSneakForSpellSet() {
        return true;
    }

    @Override
    public boolean isSocketSlotAvailable(int slot) {
        int sockets = ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.SOCKETS);
        if (sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS) {
            sockets = ItemCADSocket.MAX_SOCKETS;
        }
        return slot < sockets && slot >= 0;
    }

    @Override
    public ItemStack getBulletInSocket(int slot) {
        if (isSocketSlotAvailable(slot))
            return cadHandler.getStackInSlot(slot);
        return ItemStack.EMPTY;
    }

    @Override
    public void setBulletInSocket(int slot, ItemStack bullet) {
        if (isSocketSlotAvailable(slot))
            cadHandler.setStackInSlot(slot, bullet);
    }

    @Override
    public int getSelectedSlot() {
        return cad.getOrDefault(ModItems.TAG_SELECTED_SLOT, 0);
    }

    @Override
    public void setSelectedSlot(int slot) {
        cad.set(ModItems.TAG_SELECTED_SLOT, slot);
    }

    @Override
    public int getLastSlot() {
        int sockets = ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.SOCKETS);
        if (sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS) {
            sockets = ItemCADSocket.MAX_SOCKETS;
        }
        return sockets - 1;
    }

    @Override
    public CompoundTag serializeForSynchronization() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Time", time);
        compound.putInt("Battery", battery);

        return compound;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compound = serializeForSynchronization();

        ListTag memory = new ListTag();
        for (Vector3 vector : vectors) {
            if (vector == null) {
                memory.add(new ListTag());
            } else {
                ListTag vec = new ListTag();
                vec.add(DoubleTag.valueOf(vector.x));
                vec.add(DoubleTag.valueOf(vector.y));
                vec.add(DoubleTag.valueOf(vector.z));
                memory.add(vec);
            }
        }
        compound.put("Memory", memory);

        return compound;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("Time", Tag.TAG_ANY_NUMERIC)) {
            time = nbt.getInt("Time");
        }
        if (nbt.contains("Battery", Tag.TAG_ANY_NUMERIC)) {
            battery = nbt.getInt("Battery");
        }

        if (nbt.contains("Memory", Tag.TAG_LIST)) {
            ListTag memory = nbt.getList("Memory", Tag.TAG_LIST);
            List<Vector3> newVectors = Lists.newArrayList();
            for (int i = 0; i < memory.size(); i++) {
                ListTag vec = (ListTag) memory.get(i);
                if (vec.getElementType() == Tag.TAG_DOUBLE && vec.size() >= 3) {
                    newVectors.add(new Vector3(vec.getDouble(0), vec.getDouble(1), vec.getDouble(2)));
                } else {
                    newVectors.add(null);
                }
            }
            vectors = newVectors;
        }
    }

    @Override
    public boolean shouldShow(IPlayerData data) {
        return true;
    }
}
