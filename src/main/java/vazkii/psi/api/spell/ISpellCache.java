/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [22/01/2016, 16:32:57 (GMT)]
 */
package vazkii.psi.api.spell;

/**
 * Base interface for the spell cache. To get an instance use PsiAPI.internalHandler.getCompiler.
 * This is where compiled spells are stored. Instead of compiling a spell using a {@link ISpellCompiler},
 * use {@link #getCompiledSpell(Spell)}.
 */
public interface ISpellCache {

	/**
	 * Gets a {@link CompiledSpell} for the {@link Spell} passed in, checking the cache for it. If it's
	 * not present, a compiler is created to provide the spell.
	 */
	public CompiledSpell getCompiledSpell(Spell spell);
	
}
