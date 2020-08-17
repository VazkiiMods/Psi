/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.other;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceErrorHandler;

import javax.annotation.Nonnull;

public class PieceVectorCatch extends PieceErrorHandler {
	public PieceVectorCatch(Spell spell) {
		super(spell);
	}

	@Override
	protected String paramName() {
		return SpellParam.GENERIC_NAME_TARGET;
	}

	@Override
	public boolean catchException(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
		return errorPiece.getEvaluationType() == Vector3.class;
	}

	@Nonnull
	@Override
	public Object supplyReplacementValue(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
		return new Vector3();
	}
}
