/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.spell.SpellParam;

public class ParamAny extends SpellParam<SpellParam.Any> {

	public ParamAny(String name, int color, boolean canDisable) {
		super(name, color, canDisable);
	}

	@Override
	public Class<Any> getRequiredType() {
		return Any.class;
	}

}
