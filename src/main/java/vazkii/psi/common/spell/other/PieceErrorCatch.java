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
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Any;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamError;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.helpers.SpellHelpers;

public class PieceErrorCatch extends PieceOperator {
	SpellParam<SpellParam.Any> target, fallback;

	public PieceErrorCatch(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamError(SpellParam.GENERIC_NAME_TARGET, SpellParam.BROWN, false));
		addParam(fallback = new ParamAny(SpellParam.PSI_PREFIX + "fallback", SpellParam.GRAY, false) {
			@Override
			public boolean canAccept(SpellPiece other) {
				try {
					SpellParam.Side side = paramSides.get(target);
					SpellPiece actualPiece = spell.grid.getPieceAtSideWithRedirections(x, y, side);
					return super.canAccept(other) && actualPiece.getEvaluationType().isAssignableFrom(other.getEvaluationType());
				} catch (SpellCompilationException e) {
					return super.canAccept(other);
				}
			}
		});
	}

	@Override
	public void addModifierToMetadata(SpellMetadata meta) throws SpellCompilationException {
		SpellPiece piece = spell.grid.getPieceAtSideWithRedirections(x, y, paramSides.get(target));
		if (piece != null) {
			meta.errorSuppressed[piece.x][piece.y] = true;
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Object value = getRawParamValue(context, target);
		if (value instanceof SpellRuntimeException) {
			value = getRawParamValue(context, fallback);
		}
		return value;
	}

	@Override
	public Class<?> getEvaluationType() {
		if (!isInGrid || paramSides.get(target) == Side.OFF) {
			return Any.class;
		}
		SpellPiece piece;
		try {
			piece = spell.grid.getPieceAtSideWithRedirections(x, y, paramSides.get(target));
			if (piece == null || SpellHelpers.isLoop(this)) {
				return Any.class;
			}
			return piece.getEvaluationType();
		} catch (SpellCompilationException e) {
			return Any.class;
		}
	}
}
