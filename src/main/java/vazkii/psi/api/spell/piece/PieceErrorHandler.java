/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/01/2016, 19:09:50 (GMT)]
 */
package vazkii.psi.api.spell.piece;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;

public abstract class PieceErrorHandler extends SpellPiece implements IErrorCatcher {

	protected SpellParam piece;

	public PieceErrorHandler(Spell spell) {
		super(spell);
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.ERROR_HANDLER;
	}

	@Override
	public void initParams() {
		addParam(piece = new ParamAny(paramName(), SpellParam.BROWN, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object evaluate() {
		return null;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return null;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Null.class;
	}

	protected abstract String paramName();
}
