/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.internal.Vector3;

public class ParamVector extends ParamSpecific<Vector3> {

	public ParamVector(String name, int color, boolean canDisable) {
		super(name, color, canDisable);
	}

	@Override
	public Class<Vector3> getRequiredType() {
		return Vector3.class;
	}

}
