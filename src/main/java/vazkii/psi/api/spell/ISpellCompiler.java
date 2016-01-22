/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 15:17:35 (GMT)]
 */
package vazkii.psi.api.spell;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Base interface for a Spell Compiler. To get an instance use PsiAPI.internalHandler.getCompiler.<br>
 * For the normal compiler, compilation happens at construction time. Note: This should normally not have
 * to be used, as {@link ISpellCache} compiles spells if they're missing.
 */
public interface ISpellCompiler {

	public CompiledSpell getCompiledSpell();
	
	public String getError();
	
	public Pair<Integer, Integer> getErrorLocation();
	
	public boolean isErrored();
	
}
