/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class ParamEntityListWrapper extends ParamSpecific<EntityListWrapper> {

	public ParamEntityListWrapper(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	protected Class<EntityListWrapper> getRequiredType() {
		return EntityListWrapper.class;
	}

}
