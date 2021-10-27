/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

/**
 * An exception thrown on spell runtime. This is caught by the relevant spell
 * execution code and adds the localized value of the exception's message to
 * the player's chat.
 */
@SuppressWarnings("serial")
public class SpellRuntimeException extends Exception {

	public static final String DIVIDE_BY_ZERO = "psi.spellerror.dividebyzero";
	public static final String NULL_TARGET = "psi.spellerror.nulltarget";
	public static final String NULL_VECTOR = "psi.spellerror.nullvector";
	public static final String OUTSIDE_RADIUS = "psi.spellerror.outsideradius";
	public static final String BOSS_IMMUNE = "psi.spellerror.bossimmune";
	public static final String NO_CAD = "psi.spellerror.nocad";
	public static final String CAD_CASTING_ONLY = "psi.spellerror.cad_casting_only";
	public static final String MEMORY_OUT_OF_BOUNDS = "psi.spellerror.memoryoutofbounds";
	public static final String LOCKED_MEMORY = "psi.spellerror.lockedmemory";
	public static final String IMMUNE_TARGET = "psi.spellerror.immunetarget";
	public static final String NEGATIVE_NUMBER = "psi.spellerror.negativenumber";
	public static final String NON_POSITIVE_VALUE = "psi.spellerror.nonpositivevalue";
	public static final String NON_AXIAL_VECTOR = "psi.spellerror.nonaxial";
	public static final String OUT_OF_BOUNDS = "psi.spellerror.out_of_bounds";
	public static final String EVEN_ROOT_NEGATIVE_NUMBER = "psi.spellerror.nthroot";
	public static final String INVALID_BASE = "psi.spellerror.invalid_base";
	public static final String ARMOR = "psi.spellerror.armor";
	public static final String NO_MESSAGE = "psi.spellerror.no_message";
	public static final String COMPARATOR = "psi.spellerror.comparator";
	public static final String NAN = "psi.spellerror.nan";

	public int x, y;

	public SpellRuntimeException(String s) {
		super(s);
	}

}
