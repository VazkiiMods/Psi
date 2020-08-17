/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Base interface for a Spell Compiler. To get an instance use PsiAPI.internalHandler.getCompiler.<br>
 * For the normal compiler, compilation happens at construction time. Note: This should normally not have
 * to be used, as {@link ISpellCache} compiles spells if they're missing.
 */
public interface ISpellCompiler {

	CompiledSpell getCompiledSpell();

	String getError();

	Pair<Integer, Integer> getErrorLocation();

	void buildRedirect(SpellPiece piece) throws SpellCompilationException;

	boolean isErrored();

}
