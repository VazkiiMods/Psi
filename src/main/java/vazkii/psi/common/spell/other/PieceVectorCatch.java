package vazkii.psi.common.spell.other;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
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
