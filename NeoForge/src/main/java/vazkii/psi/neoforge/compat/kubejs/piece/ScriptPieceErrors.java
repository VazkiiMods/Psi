/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.spell.SpellRuntimeException;

import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.JavaScriptException;
import dev.latvian.mods.rhino.Undefined;
import dev.latvian.mods.rhino.Wrapper;

final class ScriptPieceErrors {

	static final String SCRIPT_ERROR = "psi.spellerror.script";
	static final String RETURN_TYPE = "psi.spellerror.script_return_type";

	private ScriptPieceErrors() {}

	@Nullable
	static Object unwrapResult(@Nullable Object result) {
		Object value = Wrapper.unwrapped(result);
		return Undefined.isUndefined(value) ? null : value;
	}

	/**
	 * Turns whatever a script callback threw into the spell error the caster sees. A
	 * {@link SpellRuntimeException} raised by the script (via {@code Psi.error}) is passed
	 * through; anything else is logged to the script console and reported as a script failure.
	 */
	static SpellRuntimeException toSpellError(ResourceLocation piece, ScriptType type, Throwable error) {
		for(Throwable cause = error; cause != null; cause = cause.getCause()) {
			if(cause instanceof SpellRuntimeException spellError) {
				return spellError;
			}
			if(cause instanceof JavaScriptException js && Wrapper.unwrapped(js.getValue()) instanceof SpellRuntimeException spellError) {
				return spellError;
			}
		}

		type.console.error("Error in spell piece " + piece, error);
		String message = error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage();
		return new SpellRuntimeException(SCRIPT_ERROR, piece.toString(), message);
	}

}
