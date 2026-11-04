/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs;

import vazkii.psi.api.spell.SpellRuntimeException;

/**
 * Exposed to scripts as {@code Psi}; {@code throw Psi.error('psi.spellerror.nulltarget')} fails
 * the running spell with that message.
 */
public final class PsiSpellBinding {

	private PsiSpellBinding() {}

	public static SpellRuntimeException error(String message, Object... arguments) {
		return new SpellRuntimeException(message, arguments);
	}

}
