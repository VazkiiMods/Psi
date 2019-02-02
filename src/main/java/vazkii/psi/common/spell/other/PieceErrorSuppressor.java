/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [30/01/2016, 17:54:56 (GMT)]
 */
package vazkii.psi.common.spell.other;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;

public class PieceErrorSuppressor extends SpellPiece {

	public PieceErrorSuppressor(Spell spell) {
		super(spell);
	}

	@Override
	public String getSortingName() {
		return "00000000001";
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		meta.errorsSuppressed = true;
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.MODIFIER;
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
	public Object execute(SpellContext context) {
		return null;
	}

}
