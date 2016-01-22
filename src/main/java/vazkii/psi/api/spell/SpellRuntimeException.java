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

/**
 * An exception thrown on spell runtime. This is caught by the relevant spell
 * execution code and adds the localized value of the exception's message to
 * the player's chat.
 */
public class SpellRuntimeException extends Exception {

	public static final String DIVIDE_BY_ZERO = "psi.spellerror.dividebyzero";
	public static final String NULL_TARGET = "psi.spellerror.nulltarget";
	public static final String NULL_VECTOR = "psi.spellerror.nullvector";
	public static final String OUTSIDE_RADIUS = "psi.spellerror.outsideradius";
	
	public SpellRuntimeException(String s) {
		super(s);
	}
	
}
