/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;

import java.util.Map;

/**
 * The script callback of a piece. {@code context} is null when the piece is being constant-folded
 * at compile time.
 */
@FunctionalInterface
public interface ScriptPieceBody {

	@Nullable
	Object run(@Nullable SpellContext context, Map<String, Object> params) throws SpellRuntimeException;

}
