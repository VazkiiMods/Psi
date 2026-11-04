/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.item.base.ModDataComponents;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.item.data.CADDataValue;

import java.util.List;

public class CADData implements ICADData, ISpellAcceptor, ISocketable, IPsiBarDisplay {

	private final ItemStack cad;

	public CADData(ItemStack cad) {
		this.cad = cad;
	}

	private CADDataValue data() {
		return cad.getOrDefault(ModDataComponents.CAD_DATA.get(), CADDataValue.EMPTY);
	}

	private void setData(CADDataValue data) {
		cad.set(ModDataComponents.CAD_DATA.get(), data);
	}

	@Override
	public int getTime() {
		return data().time();
	}

	@Override
	public void setTime(int time) {
		CADDataValue data = data();
		if(data.time() == time) {
			return;
		}
		setData(data.withTime(time));
	}

	@Override
	public int getBattery() {
		return data().battery();
	}

	@Override
	public void setBattery(int battery) {
		CADDataValue data = data();
		if(data.battery() == battery) {
			return;
		}
		setData(data.withBattery(battery));
	}

	@Override
	public Vector3 getSavedVector(int memorySlot) {
		List<Vector3> vectors = data().vectors();
		if(vectors.size() <= memorySlot) {
			return Vector3.zero.copy();
		}
		return vectors.get(memorySlot).copy();
	}

	@Override
	public void setSavedVector(int memorySlot, Vector3 value) {
		setData(data().withSavedVector(memorySlot, value));
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
	public boolean shouldShow(IPlayerData data) {
		return true;
	}

}
