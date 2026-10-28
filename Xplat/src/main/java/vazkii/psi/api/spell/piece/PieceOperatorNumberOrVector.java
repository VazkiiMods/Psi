/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.spell.piece;

import net.minecraft.network.chat.Component;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.NumberOrVector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumberOrVector;

/**
 * An operator over {@link ParamNumberOrVector} params. Evaluates to {@link Vector3} if any
 * connected param is a vector, otherwise to {@link Double}. Subclasses implement
 * {@link #compute(SpellContext)} over {@link NumberOrVector} values.
 */
public abstract class PieceOperatorNumberOrVector extends PieceOperator {

	private boolean resolvingType = false;

	public PieceOperatorNumberOrVector(Spell spell) {
		super(spell);
	}

	@Override
	public Class<?> getEvaluationType() {
		if(!isInGrid || resolvingType) {
			return Double.class;
		}

		resolvingType = true;
		try {
			for(SpellParam<?> param : params.values()) {
				if(param instanceof ParamNumberOrVector && isVector(param)) {
					return Vector3.class;
				}
			}
			return Double.class;
		} finally {
			resolvingType = false;
		}
	}

	private boolean isVector(SpellParam<?> param) {
		try {
			SpellPiece piece = getConnectedPiece(param);
			return piece != null && Vector3.class.isAssignableFrom(piece.getEvaluationType());
		} catch (SpellCompilationException e) {
			return false;
		}
	}

	@Override
	public Component getEvaluationTypeString() {
		if(!isInGrid) {
			return Component.translatable("psi.datatype.number_or_vector");
		}

		return super.getEvaluationTypeString();
	}

	@Override
	public final Object execute(SpellContext context) throws SpellRuntimeException {
		return compute(context).unwrap();
	}

	protected abstract NumberOrVector compute(SpellContext context) throws SpellRuntimeException;

	protected NumberOrVector getNumberOrVector(SpellContext context, ParamNumberOrVector param) throws SpellRuntimeException {
		Object raw = getParamValue(context, param);
		return raw == null ? null : NumberOrVector.of(raw);
	}

}
