/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.cad.RegenPsiEvent;
import vazkii.psi.api.internal.IPlayerData;

public class RegenPsiKubeEvent extends PsiKubeEvent {

	private final RegenPsiEvent event;

	public RegenPsiKubeEvent(RegenPsiEvent event) {
		super(event);
		this.event = event;
	}

	public Player getPlayer() {
		return event.getPlayer();
	}

	public IPlayerData getPlayerData() {
		return event.getPlayerData();
	}

	public ItemStack getCad() {
		return event.getCad();
	}

	public int getPlayerPsiCapacity() {
		return event.getPlayerPsiCapacity();
	}

	public int getPlayerPsi() {
		return event.getPlayerPsi();
	}

	public int getCadPsiCapacity() {
		return event.getCadPsiCapacity();
	}

	public int getCadPsi() {
		return event.getCadPsi();
	}

	public int getRegenRate() {
		return event.getRegenRate();
	}

	public int getBaseRegenRate() {
		return event.getBaseRegenRate();
	}

	public int getPreviousRegenCooldown() {
		return event.getPreviousRegenCooldown();
	}

	public boolean wasOverflowed() {
		return event.wasOverflowed();
	}

	public int getPlayerRegen() {
		return event.getPlayerRegen();
	}

	public int getCadRegen() {
		return event.getCadRegen();
	}

	public int getCadRegenCost() {
		return event.getCadRegenCost();
	}

	public boolean willHealOverflow() {
		return event.willHealOverflow();
	}

	public int getRegenCooldown() {
		return event.getRegenCooldown();
	}

	public void setRegenCooldown(int regenCooldown) {
		event.setRegenCooldown(regenCooldown);
	}

	public void addRegen(int amount) {
		event.addRegen(amount);
	}

	public void removeRegen(int amount) {
		event.removeRegen(amount);
	}

	public int getMaxPlayerRegen() {
		return event.getMaxPlayerRegen();
	}

	public void setMaxPlayerRegen(int maxPlayerRegen) {
		event.setMaxPlayerRegen(maxPlayerRegen);
	}

	public int getMaxCadRegen() {
		return event.getMaxCadRegen();
	}

	public void setMaxCadRegen(int maxCadRegen) {
		event.setMaxCadRegen(maxCadRegen);
	}

	public boolean willRegenCadFirst() {
		return event.willRegenCadFirst();
	}

	public void regenCadFirst(boolean regenCadFirst) {
		event.regenCadFirst(regenCadFirst);
	}

}
