/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 22:24:15 (GMT)]
 */
package vazkii.psi.api.spell.param;

public class ParamNumber extends ParamSpecific {

	public ParamNumber(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	public Class<?> getRequiredType() {
		return Number.class;
	}

}
