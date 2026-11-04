/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import net.minecraft.world.entity.player.Player;

/**
 * Base interface for the spell cache. To get an instance use PsiAPI.internalHandler.getCompiler.
 * This is where compiled spells are stored. Instead of compiling a spell using a {@link ISpellCompiler},
 * use {@link #getCompiledSpell(Spell, Player)}.
 */
public interface ISpellCache {

	/**
	 * Gets a {@link CompiledSpell} for the {@link Spell} passed in, checking the cache for it. If it's
	 * not present, a compiler is created to provide the spell. Returns null if the spell does not
	 * compile for {@code player}.
	 */
	CompiledSpell getCompiledSpell(Spell spell, Player player);

}
