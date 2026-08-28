/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import com.google.common.collect.Lists;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.data.CADDataValue;

import java.util.ArrayList;
import java.util.List;

public class CADData implements ICADData, ISpellAcceptor, ISocketable, IPsiBarDisplay {

	private final ItemStack cad;
	private final CADDataValue data;

	public CADData(ItemStack cad) {
		this.cad = cad;
		CADDataValue cadData = cad.get(ModDataComponents.CAD_DATA.get());

		if(cadData == null) {
			cadData = new CADDataValue(0, 0, new ArrayList<>());
			cad.set(ModDataComponents.CAD_DATA.get(), cadData);
		}

		this.data = cadData;
	}

	@Override
	public int getTime() {
		return data.time;
	}

	@Override
	public void setTime(int time) {
		if(this.data.time != time) {
			this.data.time = time;
		}
	}

	@Override
	public int getBattery() {
		return data.battery;
	}

	@Override
	public void setBattery(int battery) {
		this.data.battery = battery;
	}

	@Override
	public Vector3 getSavedVector(int memorySlot) {
		if(data.vectors.size() <= memorySlot) {
			return Vector3.zero.copy();
		}

		Vector3 vec = data.vectors.get(memorySlot);
		return (vec == null ? Vector3.zero : vec).copy();
	}

	@Override
	public void setSavedVector(int memorySlot, Vector3 value) {
		while(data.vectors.size() <= memorySlot) {
			data.vectors.add(null);
		}

		data.vectors.set(memorySlot, value);
	}

	@Override
	public void setSpell(Player player, Spell spell) {
		int slot = getSelectedSlot();
		ItemStack bullet = getBulletInSocket(slot);
		if(!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
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
		if(!(cad.getItem() instanceof ICAD)) {
			return false;
		}

		int sockets = ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.SOCKETS);
		if(sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS) {
			sockets = ItemCADSocket.MAX_SOCKETS;
		}
		return slot < sockets && slot >= 0;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		if(isSocketSlotAvailable(slot))
			return bullets().get(slot);
		return ItemStack.EMPTY;
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		if(isSocketSlotAvailable(slot)) {
			NonNullList<ItemStack> bullets = bullets();
			bullets.set(slot, bullet.copy());
			cad.set(ModDataComponents.BULLETS.get(), ItemContainerContents.fromItems(bullets));
		}
	}

	private NonNullList<ItemStack> bullets() {
		NonNullList<ItemStack> bullets = NonNullList.withSize(ISocketable.MAX_ASSEMBLER_SLOTS, ItemStack.EMPTY);
		cad.getOrDefault(ModDataComponents.BULLETS.get(), ItemContainerContents.EMPTY).copyInto(bullets);
		return bullets;
	}

	@Override
	public int getSelectedSlot() {
		return cad.getOrDefault(ModDataComponents.SELECTED_SLOT.get(), 0);
	}

	@Override
	public void setSelectedSlot(int slot) {
		cad.set(ModDataComponents.SELECTED_SLOT.get(), slot);
	}

	@Override
	public int getLastSlot() {
		int sockets = ((ICAD) cad.getItem()).getStatValue(cad, EnumCADStat.SOCKETS);
		if(sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS) {
			sockets = ItemCADSocket.MAX_SOCKETS;
		}
		return sockets - 1;
	}

	@Override
	public CompoundTag serializeForSynchronization() {
		CompoundTag compound = new CompoundTag();
		compound.putInt("Time", data.time);
		compound.putInt("Battery", data.battery);

		return compound;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
		CompoundTag compound = serializeForSynchronization();

		ListTag memory = new ListTag();
		for(Vector3 vector : data.vectors) {
			if(vector == null) {
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
	public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
		if(nbt.contains("Time", Tag.TAG_ANY_NUMERIC)) {
			data.time = nbt.getInt("Time");
		}
		if(nbt.contains("Battery", Tag.TAG_ANY_NUMERIC)) {
			data.battery = nbt.getInt("Battery");
		}

		if(nbt.contains("Memory", Tag.TAG_LIST)) {
			ListTag memory = nbt.getList("Memory", Tag.TAG_LIST);
			List<Vector3> newVectors = Lists.newArrayList();
			for(Tag tag : memory) {
				ListTag vec = (ListTag) tag;
				if(vec.getElementType() == Tag.TAG_DOUBLE && vec.size() >= 3) {
					newVectors.add(new Vector3(vec.getDouble(0), vec.getDouble(1), vec.getDouble(2)));
				} else {
					newVectors.add(null);
				}
			}
			data.vectors = newVectors;
		}
	}

	@Override
	public boolean shouldShow(IPlayerData data) {
		return true;
	}

}
