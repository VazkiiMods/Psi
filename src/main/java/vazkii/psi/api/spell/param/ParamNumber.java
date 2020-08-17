/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

public class ParamNumber extends ParamSpecific<Number> {

	public ParamNumber(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	public Class<Number> getRequiredType() {
		return Number.class;
	}

}
