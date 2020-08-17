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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class CADData implements ICapabilityProvider, ICADData, ISpellAcceptor, ISocketable, IPsiBarDisplay {

	private final ItemStack cad;
	private int time;
	private int battery;
	private List<Vector3> vectors = Lists.newArrayList();

	private boolean dirty;

	private final LazyOptional<?> optional;

	public CADData(ItemStack cad) {
		this.cad = cad;
		optional = LazyOptional.of(() -> this);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (capability == PsiAPI.SOCKETABLE_CAPABILITY
				|| capability == PsiAPI.CAD_DATA_CAPABILITY
				|| capability == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY
				|| capability == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) {
			return optional.cast();
		}
		return LazyOptional.empty();
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
	public void setSpell(PlayerEntity player, Spell spell) {
		int slot = getSelectedSlot();
		ItemStack bullet = getBulletInSocket(slot);
		if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(slot, bullet);
			player.getCooldownTracker().setCooldown(cad.getItem(), 10);
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
		return slot < sockets;
	}

	@Override
	public ItemStack getBulletInSocket(int slot) {
		String name = IPsimetalTool.TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = cad.getOrCreateTag().getCompound(name);

		if (cmp.isEmpty()) {
			return ItemStack.EMPTY;
		}

		return ItemStack.read(cmp);
	}

	@Override
	public void setBulletInSocket(int slot, ItemStack bullet) {
		String name = IPsimetalTool.TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = new CompoundNBT();

		if (!bullet.isEmpty()) {
			bullet.write(cmp);
		}

		cad.getOrCreateTag().put(name, cmp);
	}

	@Override
	public int getSelectedSlot() {
		return cad.getOrCreateTag().getInt(IPsimetalTool.TAG_SELECTED_SLOT);
	}

	@Override
	public void setSelectedSlot(int slot) {
		cad.getOrCreateTag().putInt(IPsimetalTool.TAG_SELECTED_SLOT, slot);
	}

	@Override
	public CompoundNBT serializeForSynchronization() {
		CompoundNBT compound = new CompoundNBT();
		compound.putInt("Time", time);
		compound.putInt("Battery", battery);

		return compound;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT compound = serializeForSynchronization();

		ListNBT memory = new ListNBT();
		for (Vector3 vector : vectors) {
			if (vector == null) {
				memory.add(new ListNBT());
			} else {
				ListNBT vec = new ListNBT();
				vec.add(DoubleNBT.of(vector.x));
				vec.add(DoubleNBT.of(vector.y));
				vec.add(DoubleNBT.of(vector.z));
				memory.add(vec);
			}
		}
		compound.put("Memory", memory);

		return compound;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("Time", Constants.NBT.TAG_ANY_NUMERIC)) {
			time = nbt.getInt("Time");
		}
		if (nbt.contains("Battery", Constants.NBT.TAG_ANY_NUMERIC)) {
			battery = nbt.getInt("Battery");
		}

		if (nbt.contains("Memory", Constants.NBT.TAG_LIST)) {
			ListNBT memory = nbt.getList("Memory", Constants.NBT.TAG_LIST);
			List<Vector3> newVectors = Lists.newArrayList();
			for (int i = 0; i < memory.size(); i++) {
				ListNBT vec = (ListNBT) memory.get(i);
				if (vec.getElementType() == Constants.NBT.TAG_DOUBLE && vec.size() >= 3) {
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
