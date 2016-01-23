/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [13/01/2016, 17:01:43 (GMT)]
 */
package vazkii.psi.api.internal;

/**
 * This is a dummy class. You'll never interact with it, it's just here so
 * in case something goes really wrong the field isn't null.
 */
public class DummyPlayerData implements IPlayerData {

	@Override
	public int getLevel() {
		return 0;
	}
	
	@Override
	public int getTotalPsi() {
		return 0;
	}

	@Override
	public int getAvailablePsi() {
		return 0;
	}

	@Override
	public int getLastAvailablePsi() {
		return 0;
	}

	@Override
	public int getRegenCooldown() {
		return 0;
	}

	@Override
	public int getRegenPerTick() {
		return 0;
	}

	@Override
	public void deductPsi(int psi, int cd, boolean sync, boolean shatter) {
		// NO-OP
	}

	@Override
	public boolean isSpellGroupUnlocked(String group) {
		return false;
	}

	@Override
	public void unlockSpellGroup(String group) {
		// NO-OP
	}

}
