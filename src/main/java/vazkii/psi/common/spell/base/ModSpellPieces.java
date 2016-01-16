/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 16:10:43 (GMT)]
 */
package vazkii.psi.common.spell.base;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibSpellNames;
import vazkii.psi.common.spell.selector.PieceSelectorCaster;
import vazkii.psi.common.spell.trick.PieceTrickDebug;

public final class ModSpellPieces {

	public static PieceContainer selectorCaster;
	
	public static PieceContainer trickDebug;
	
	public static void init() {
		selectorCaster = register(PieceSelectorCaster.class, LibSpellNames.SELECTOR_CASTER);
		
		trickDebug = register(PieceTrickDebug.class, LibSpellNames.TRICK_DEBUG);
	}
	
	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name) {
		PsiAPI.registerSpellPieceAndTexture(name, clazz);
		return (Spell s) -> { return SpellPiece.create(clazz, s); };
	}
	
	public static interface PieceContainer {
		public SpellPiece get(Spell s);
	}
	
}
