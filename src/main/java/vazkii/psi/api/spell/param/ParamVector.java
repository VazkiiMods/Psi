/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [18/01/2016, 21:56:42 (GMT)]
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.internal.Vector3;

public class ParamVector extends ParamSpecific {

	public ParamVector(String name, int color, boolean canDisable, boolean constant) {
		super(name, color, canDisable, constant);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
