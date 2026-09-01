/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import vazkii.psi.api.spell.SpellParam;

/**
 * A script-declared parameter. {@code name} is the key scripts read the value under; the
 * programmer labels it with the conventional {@code psi.spellparam.<name>} translation.
 */
public record ScriptPieceParam(String name, ScriptValueType type, boolean canDisable) {

	public SpellParam<?> create() {
		return type.newParam(SpellParam.PSI_PREFIX + name, canDisable);
	}

}
