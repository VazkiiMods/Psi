/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 16:58:22 (GMT)]
 */
package vazkii.psi.api.internal;

import vazkii.psi.api.spell.SpellPiece;

public interface IPlayerData {

	/**
	 * Gets the player's level. May be 0.
	 */
	public int getLevel();
	
	/**
	 * Gets the total amount of psi energy the player has.
	 * Does not include the amount in the player's CAD battery.
	 */
	public int getTotalPsi();
	
	/**
	 * Gets the current amount of psi energy the player has.
	 * Does not include the amount in the player's CAD battery.
	 */
	public int getAvailablePsi();
	
	/**
	 * Gets the current amount of psi energy the player had last tick.
	 * Not guaranteed to synchronise properly or be 100% accurate. Use with caution.
	 * Does not include the amount in the player's CAD battery.
	 */
	public int getLastAvailablePsi();
	
	/**
	 * Gets the cooldown before the player can start regenerating psi energy.
	 */
	public int getRegenCooldown();

	/**
	 * Gets how much psi the player regenerates per tick.
	 */
	public int getRegenPerTick();
	
	/**
	 * Deducts the amount of psi given from the player's psi energy.
	 * This will not check against the available amount. Any extra will be either
	 * deducted from the player's CAD battery, or deducted as damage.
	 */
	public void deductPsi(int psi, int cd, boolean sync, boolean shatter);
	
	/**
	 * Gets it the piece group name is unlocked.
	 */
	public boolean isPieceGroupUnlocked(String group);
	
	/**
	 * Unlocks the given piece group.
	 */
	public void unlockPieceGroup(String group);
	
	/**
	 * Marks a spell piece as executed. Used for leveling.
	 */
	public void markPieceExecuted(SpellPiece piece);

}
