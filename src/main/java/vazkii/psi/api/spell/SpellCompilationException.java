/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import org.apache.commons.lang3.tuple.Pair;

/**
 * An exception thrown on spell compilation. This is caught by the programmer GUI
 * and will show up as an error in it, as well as its {@link #location} as a "warning"
 * display next to the piece it indicates.
 */
@SuppressWarnings("serial")
public class SpellCompilationException extends Exception {

	public static final String NO_SPELL = "psi.spellerror.nospell";
	public static final String NO_NAME = "psi.spellerror.noname";
	public static final String NO_TRICKS = "psi.spellerror.notricks";
	public static final String UNSET_PARAM = "psi.spellerror.unsetparam";
	public static final String NULL_PARAM = "psi.spellerror.nullparam";
	public static final String INVALID_PARAM = "psi.spellerror.invalidparam";
	public static final String SAME_SIDE_PARAMS = "psi.spellerror.samesideparams";
	public static final String INFINITE_LOOP = "psi.spellerror.loop";
	public static final String NON_POSITIVE_VALUE = "psi.spellerror.nonpositivevalue";
	public static final String NON_INTEGER = "psi.spellerror.noninteger";
	public static final String NON_POSITIVE_INTEGER = "psi.spellerror.nonpositiveinteger";
	public static final String STAT_OVERFLOW = "psi.spellerror.statoverflow";
	public static final String PITCH = "psi.spellerror.pitch";
	public static final String VOLUME = "psi.spellerror.volume";
	public static final String INSTRUMENTS = "psi.spellerror.instruments";

	public final Pair<Integer, Integer> location;

	public SpellCompilationException(String s) {
		this(s, -1, -1);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public SpellCompilationException(String s, int x, int y) {
		super(s);
		location = Pair.of(x, y);
	}

}
