/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 22:18:42 (GMT)]
 */
package vazkii.psi.api.spell;

import org.apache.commons.lang3.tuple.Pair;

public class SpellCompilationException extends Exception {

	public static final String NO_SPELL = "psi.spellerror.nospell";
	public static final String NO_TRICKS = "psi.spellerror.notricks";
	public static final String UNSET_PARAM = "psi.spellerror.unsetparam";
	public static final String NULL_PARAM = "psi.spellerror.nullparam";
	public static final String INVALID_PARAM = "psi.spellerror.invalidparam";
	public static final String SAME_SIDE_PARAMS = "psi.spellerror.samesideprams";
	public static final String INFINITE_LOOP = "psi.spellerror.loop";
	public static final String NON_POSITIVE_VALUE = "psi.spellerror.nonpositivevalue";

	public final Pair<Integer, Integer> location;
	
	public SpellCompilationException(String s) {
		this(s, -1, -1);
	}
	
	public SpellCompilationException(String s, int x, int y) {
		super(s);
		location = Pair.of(x, y);
	}
	
}