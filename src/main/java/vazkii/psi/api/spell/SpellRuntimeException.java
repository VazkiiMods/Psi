/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [18/01/2016, 19:42:22 (GMT)]
 */
package vazkii.psi.api.spell;

public class SpellRuntimeException extends Exception {

	public SpellRuntimeException(String s) {
		super("psi.spellerror." + s);
	}
	
}
