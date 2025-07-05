/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import vazkii.psi.api.internal.IPlayerData;

/**
 * Posted every tick as a player regenerates PSI.
 * <p>
 * This event is posted even when the player is on regen cooldown,
 * and can be used to control the cooldown time.
 * <p>
 * This event is {@link ICancellableEvent}.
 * If canceled, no regen will occur and the regen cooldown will not change.
 */
public class RegenPsiEvent extends Event implements ICancellableEvent {

	private final int playerPsiCapacity;
	private final int playerPsi;
	private final int cadPsiCapacity;
	private final int cadPsi;
	private final boolean wasOverflowed;
	private final Player player;
	private final IPlayerData playerData;
	private final ItemStack cad;
	private final int previousRegenCooldown;
	private final int baseRegenRate;

	// Cannot be set externally.
	private int regenRate;
	private int cadRegenCost = 0;

	private boolean regenCadFirst = true;
	private int maxPlayerRegen = -1;
	private int maxCadRegen = -1;

	private int playerRegen = 0;
	private int cadRegen = 0;
	private boolean healOverflow = false;

	private int regenCooldown;

	public RegenPsiEvent(Player player, IPlayerData playerData, ItemStack cad) {
		this.playerPsiCapacity = playerData.getTotalPsi();
		this.playerPsi = playerData.getAvailablePsi();
		this.regenRate = this.baseRegenRate = playerData.getRegenPerTick();
		this.wasOverflowed = playerData.isOverflowed();
		this.previousRegenCooldown = playerData.getRegenCooldown();
		this.regenCooldown = Math.max(0, this.previousRegenCooldown - 1);

		if(!cad.isEmpty()) {
			ICAD cadItem = (ICAD) cad.getItem();
			this.cadPsiCapacity = cadItem.getStatValue(cad, EnumCADStat.OVERFLOW);
			this.cadPsi = cadItem.getStoredPsi(cad);
		} else {
			this.cadPsiCapacity = this.cadPsi = 0;
		}

		this.player = player;
		this.playerData = playerData;
		this.cad = cad;

		applyRegen();
	}

	public Player getPlayer() {
		return player;
	}

	public IPlayerData getPlayerData() {
		return playerData;
	}

	public ItemStack getCad() {
		return cad;
	}

	/**
	 * Gets the capacity of the player's internal Psi reserves. Is usually 5000.
	 */
	public int getPlayerPsiCapacity() {
		return playerPsiCapacity;
	}

	/**
	 * Gets the current Psi energy the player has, before regeneration.
	 * Is always less than or equal to {@link #getPlayerPsiCapacity()}.
	 */
	public int getPlayerPsi() {
		return playerPsi;
	}

	/**
	 * Gets the capacity of the CAD's internal Psi reserves. Varies by CAD battery.
	 */
	public int getCadPsiCapacity() {
		return cadPsiCapacity;
	}

	/**
	 * Gets the current Psi energy the player has.
	 * In the case of a capacity of -1, this will most likely be 0.
	 * Otherwise, is always less than or equal to {@link #getCadPsiCapacity()}.
	 */
	public int getCadPsi() {
		return cadPsi;
	}

	/**
	 * Gets the current Psi regen rate the player has, modified by events.
	 */
	public int getRegenRate() {
		return regenRate;
	}

	/**
	 * Gets the current base Psi regen rate the player has.
	 */
	public int getBaseRegenRate() {
		return baseRegenRate;
	}

	/**
	 * Gets the regen cooldown from the previous tick.
	 */
	public int getPreviousRegenCooldown() {
		return previousRegenCooldown;
	}

	/**
	 * Gets whether the player was overflowed before regen started.
	 */
	public boolean wasOverflowed() {
		return wasOverflowed;
	}

	/**
	 * Gets the amount of Psi the player's reserves will regenerate this tick.
	 * If the CAD is not full, this is typically 0. Addons may alter this behavior.
	 */
	public int getPlayerRegen() {
		return playerRegen;
	}

	/**
	 * Gets the amount of Psi the CAD's reserves will regenerate this tick.
	 * If the CAD has no battery, this value has no effect.
	 */
	public int getCadRegen() {
		return cadRegen;
	}

	/**
	 * Gets how much of the player's regen it cost to regenerate CAD Psi.
	 * This value exists because batteries regenerate half as fast as the player's reserves,
	 * meaning that they cost twice as much "regen points" to fill.
	 * This value has no effect on the final values of regen.
	 */
	public int getCadRegenCost() {
		return cadRegenCost;
	}

	/**
	 * Gets whether the player will heal from overflow this tick.
	 * Typically, this value indicates the player has regenerated more Psi than they can store.
	 */
	public boolean willHealOverflow() {
		return healOverflow;
	}

	/**
	 * Gets the player's current regen cooldown.
	 */
	public int getRegenCooldown() {
		return regenCooldown;
	}

	/**
	 * Sets the player's current regen cooldown.
	 * Setting this value to 0 will calculate Psi regeneration this tick.
	 */
	public void setRegenCooldown(int regenCooldown) {
		this.regenCooldown = regenCooldown;
		applyRegen();
	}

	/**
	 * Adds extra regen, applying the default logic for regeneration.
	 * Call this for simple regen modifiers.
	 */
	public void addRegen(int amount) {
		regenRate += amount;
		applyRegen();
	}

	/**
	 * Subtracts regen, applying the default logic for regeneration in reverse order.
	 * Call this for simple regen modifiers.
	 * <p>
	 * If the new regen rate is below zero, it will not change the Psi values.
	 */
	public void removeRegen(int amount) {
		regenRate -= amount;
		applyRegen();
	}

	/**
	 * The maximum amount the player is allowed to regenerate Psi this tick.
	 * Defaults to -1, which implies no limit.
	 */
	public int getMaxPlayerRegen() {
		return maxPlayerRegen;
	}

	/**
	 * Sets the maximum amount the player is allowed to regenerate Psi this tick.
	 */
	public void setMaxPlayerRegen(int maxPlayerRegen) {
		this.maxPlayerRegen = maxPlayerRegen;
		applyRegen();
	}

	/**
	 * The maximum amount the CAD battery is allowed to regenerate Psi this tick.
	 * Defaults to -1, which implies infinity.
	 */
	public int getMaxCadRegen() {
		return maxCadRegen;
	}

	/**
	 * Sets the maximum amount the CAD battery is allowed to regenerate Psi this tick.
	 */
	public void setMaxCadRegen(int maxCadRegen) {
		this.maxCadRegen = maxCadRegen;
		applyRegen();
	}

	/**
	 * Whether CAD or player regeneration is calculated first.
	 */
	public boolean willRegenCadFirst() {
		return regenCadFirst;
	}

	/**
	 * Sets whether CAD or player regeneration is calculated first.
	 */
	public void regenCadFirst(boolean regenCadFirst) {
		this.regenCadFirst = regenCadFirst;
		applyRegen();
	}

	private void applyRegen() {
		if(regenCooldown != 0) {
			return;
		}

		cadRegenCost = 0;
		cadRegen = 0;
		playerRegen = 0;

		int regenLeft = regenRate;

		if(regenCadFirst) {
			regenLeft = applyCadRegen(regenLeft);
		}
		regenLeft = applyPlayerRegen(regenLeft);
		if(!regenCadFirst) {
			applyCadRegen(regenLeft);
		}
	}

	private int applyPlayerRegen(int regenLeft) {
		int playerRegenTotal = Math.min(playerPsiCapacity - playerPsi, regenLeft);
		if(maxPlayerRegen >= 0) {
			playerRegenTotal = Math.min(maxPlayerRegen, playerRegenTotal);
		}

		if(regenLeft > 0 && playerRegenTotal > 0) {
			playerRegen = playerRegenTotal;
			regenLeft -= playerRegenTotal;
		} else {
			playerRegen = 0;
		}

		healOverflow = regenLeft > 0;
		if(healOverflow && wasOverflowed) {
			regenLeft--;
		}

		return regenLeft;
	}

	private int applyCadRegen(int regenLeft) {
		int cadRegenTotal = cadPsiCapacity - cadPsi;
		if(maxCadRegen >= 0) {
			cadRegenTotal = Math.min(maxCadRegen, cadRegenTotal);
		}

		cadRegenCost = Math.min(regenLeft, cadRegenTotal * 2);

		if(cadRegenCost > 0) {
			cadRegen = Math.min(Math.max(1, cadRegenCost / 2), cadRegenTotal);
			regenLeft -= cadRegenCost;
		} else {
			cadRegen = 0;
		}
		return regenLeft;
	}
}
