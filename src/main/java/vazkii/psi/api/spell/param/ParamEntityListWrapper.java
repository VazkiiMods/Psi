/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 00:47:31 (GMT)]
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class ParamEntityListWrapper extends ParamSpecific {

	public ParamEntityListWrapper(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	protected Class<?> getRequiredType() {
		return EntityListWrapper.class;
	}


}
