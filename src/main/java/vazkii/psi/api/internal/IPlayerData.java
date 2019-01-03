/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 16:58:22 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.SpellPiece;

public interface IPlayerData {

	/**
	 * Gets the player's level. May be 0.
	 */
	int getLevel();

	/**
	 * Gets the total amount of psi energy the player has.
	 * Does not include the amount in the player's CAD battery.
	 */
	int getTotalPsi();

	/**
	 * Gets the current amount of psi energy the player has.
	 * Does not include the amount in the player's CAD battery.
	 */
	int getAvailablePsi();

	/**
	 * Gets the current amount of psi energy the player had last tick.
	 * Not guaranteed to synchronise properly or be 100% accurate. Use with caution.
	 * Does not include the amount in the player's CAD battery.
	 */
	int getLastAvailablePsi();

	/**
	 * Gets the cooldown before the player can start regenerating psi energy.
	 */
	int getRegenCooldown();

	/**
	 * Gets how much psi the player regenerates per tick.
	 */
	int getRegenPerTick();

	/**
	 * Deducts the amount of psi given from the player's psi energy.
	 * This will not check against the available amount. Any extra will be either
	 * deducted from the player's CAD battery, or deducted as damage.
	 */
	void deductPsi(int psi, int cd, boolean sync, boolean shatter);

	/**
	 * Gets it the piece group name is unlocked.
	 */
	boolean isPieceGroupUnlocked(String group);

	/**
	 * Unlocks the given piece group.
	 */
	void unlockPieceGroup(String group);

	/**
	 * Marks a spell piece as executed. Used for leveling.
	 */
	void markPieceExecuted(SpellPiece piece);

	/**
	 * Gets a tag compound where you can put your own stuff. If you're
	 * going to write any data here, please ensure it's prefixed with
	 * your mod ID so stuff doesn't get written over other stuff.
	 */
	NBTTagCompound getCustomData();
	
	/**
	 * Saves the data to the player entity's NBT tags. 
	 */
	void save();
	
}
