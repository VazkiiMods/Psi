/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

/**
 * Base interface for a Spell Compiler. To get an instance use PsiAPI.internalHandler.getCompiler.<br>
 * For the normal compiler, compilation happens at construction time. Note: This should normally not have
 * to be used, as {@link ISpellCache} compiles spells if they're missing.
 */
public interface ISpellCompiler {
	/**
	 * Compiles the spell for {@code player}, failing on pieces locked for them
	 * (see {@code PsiAPI.isPieceLocked}). With no player, only disabled pieces fail.
	 */
	Either<CompiledSpell, SpellCompilationException> compile(Spell in, RegistryAccess registries, @Nullable Player player);
}
