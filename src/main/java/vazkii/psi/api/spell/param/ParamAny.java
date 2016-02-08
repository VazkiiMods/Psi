/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [16/01/2016, 19:58:37 (GMT)]
 */
package vazkii.psi.api.spell.param;

import net.minecraft.util.StatCollector;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;

public class ParamAny extends SpellParam {

	public ParamAny(String name, int color, boolean canDisable) {
		super(name, color, canDisable);
	}

	@Override
	public Class<?> getRequiredType() {
		return Any.class;
	}
	

}
