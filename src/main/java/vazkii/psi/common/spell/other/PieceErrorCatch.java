/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.other;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceErrorHandler;

import javax.annotation.Nonnull;

public class PieceErrorCatch extends PieceErrorHandler {
	SpellParam<SpellParam.Any> fallback;

	public PieceErrorCatch(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		super.initParams();
		addParam(fallback = new ParamAny(SpellParam.PSI_PREFIX + "fallback", SpellParam.GRAY, false) {
			@Override
			public boolean canAccept(SpellPiece other) {
				try {
					SpellParam.Side side = paramSides.get(piece);
					SpellPiece actualPiece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
					return super.canAccept(other) && actualPiece.getEvaluationType().isAssignableFrom(other.getEvaluationType());
				} catch (SpellCompilationException e) {
					return super.canAccept(other);
				}
			}
		});
	}

	@Override
	protected String paramName() {
		return SpellParam.GENERIC_NAME_TARGET;
	}

	@Override
	public boolean catchException(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
		try {
			SpellParam.Side side = paramSides.get(piece);
			SpellPiece actualPiece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
			return errorPiece == actualPiece;
		} catch (SpellCompilationException e) {
			return false;
		}
	}

	@Nonnull
	@Override
	public Object supplyReplacementValue(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
		return getRawParamValue(context, fallback);
	}
}
