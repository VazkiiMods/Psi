/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Any;
import vazkii.psi.api.spell.SpellParam.ArrowType;

public class ParamAny extends SpellParam<SpellParam.Any> {

	public ParamAny(String name, int color, boolean canDisable) {
		super(name, color, canDisable);
	}

	public ParamAny(String name, int color, boolean canDisable, ArrowType arrowType) {
		super(name, color, canDisable, arrowType);
	}

	@Override
	public Class<Any> getRequiredType() {
		return Any.class;
	}

}
