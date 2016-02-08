/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [20/01/2016, 23:01:29 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorFocalPoint extends PieceSelector {

	public PieceSelectorFocalPoint(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return context.focalPoint;
	}

}
