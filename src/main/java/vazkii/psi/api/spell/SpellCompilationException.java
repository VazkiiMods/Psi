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

	public final Pair<Integer, Integer> location;
	
	public SpellCompilationException(String s) {
		this(s, -1, -1);
	}
	
	public SpellCompilationException(String s, int x, int y) {
		super("psi.spellerror." + s);
		location = Pair.of(x, y);
	}
	
}