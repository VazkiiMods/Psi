/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:13:56 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorCaster extends PieceSelector {

	public PieceSelectorCaster(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityPlayer.class;
	}

	@Override
	public Object execute(SpellContext context) {
		return context.caster;
	}


}
