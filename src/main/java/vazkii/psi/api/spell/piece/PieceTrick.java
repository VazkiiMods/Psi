/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/01/2016, 15:04:04 (GMT)]
 */
package vazkii.psi.api.spell.piece;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

public abstract class PieceTrick extends SpellPiece {

	public PieceTrick(Spell spell) {
		super(spell);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.TRICK;
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
		meta.addStat(EnumSpellStat.PROJECTION, 1);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Null.class;
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return null;
	}
	
	public double multiplySafe(double v1, double... arr) throws SpellCompilationException {
		double a = v1;
		for(int i = 0; i < arr.length; i++) {
			double b = arr[i];
			
			a = a * b;
			if((int) a < 0 || (int) a == Integer.MAX_VALUE)
				throw new SpellCompilationException(SpellCompilationException.STAT_OVERFLOW);
		}
		
		return a;
	}

}
