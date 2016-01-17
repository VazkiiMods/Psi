/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/01/2016, 15:03:32 (GMT)]
 */
package vazkii.psi.api.spell.param;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.SpellPiece;

public class ParamNumberConstant extends ParamNumber {

	public ParamNumberConstant(String name, int color, boolean canDisable) {
		super(name, color, canDisable);
	}
	
	@Override
	public boolean canAccept(SpellPiece piece) {
		return super.canAccept(piece) && piece.getPieceType() == EnumPieceType.CONSTANT;
	}

}
