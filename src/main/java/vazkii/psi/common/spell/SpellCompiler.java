/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell;

import com.mojang.datafixers.util.Either;

import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.CompiledSpell.Action;
import vazkii.psi.api.spell.CompiledSpell.CatchHandler;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.IErrorCatcher;
import vazkii.psi.api.spell.ISpellCompiler;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/* Probably not thread-safe. */
public final class SpellCompiler implements ISpellCompiler {

	/** The current spell being compiled. */
	private CompiledSpell compiled;

	private final Set<SpellPiece> redirectionPieces = new HashSet<>();

	@Override
	public Either<CompiledSpell, SpellCompilationException> compile(Spell in) {
		try {
			return Either.left(doCompile(in));
		} catch (SpellCompilationException e) {
			return Either.right(e);
		}
	}

	public CompiledSpell doCompile(Spell spell) throws SpellCompilationException {
		if(spell == null) {
			throw new SpellCompilationException(SpellCompilationException.NO_SPELL);
		}

		redirectionPieces.clear();
		compiled = new CompiledSpell(spell);

		for(SpellPiece piece : findPieces(EnumPieceType.ERROR_HANDLER::equals)) {
			buildHandler(piece);
		}

		List<SpellPiece> tricks = findPieces(EnumPieceType::isTrick);
		if(tricks.isEmpty()) {
			throw new SpellCompilationException(SpellCompilationException.NO_TRICKS);
		}
		for(SpellPiece trick : tricks) {
			buildPiece(trick);
		}

		if(compiled.metadata.getStat(EnumSpellStat.COST) < 0 || compiled.metadata.getStat(EnumSpellStat.POTENCY) < 0) {
			throw new SpellCompilationException(SpellCompilationException.STAT_OVERFLOW);
		}

		if(spell.name == null || spell.name.isEmpty()) {
			throw new SpellCompilationException(SpellCompilationException.NO_NAME);
		}
		return compiled;
	}

	public void buildPiece(SpellPiece piece) throws SpellCompilationException {
		buildPiece(piece, new HashSet<>());
	}

	public void buildPiece(SpellPiece piece, Set<SpellPiece> visited) throws SpellCompilationException {
		if(!visited.add(piece)) {
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP, piece.x, piece.y);
		}

		if(compiled.actionMap.containsKey(piece)) { // move to top
			Action a = compiled.actionMap.get(piece);
			compiled.actions.remove(a);
			compiled.actions.add(a);
		} else {
			Action a = compiled.new Action(piece);
			compiled.actions.add(a);
			compiled.actionMap.put(piece, a);
			try {
				piece.addToMetadata(compiled.metadata);
			} catch (NullPointerException e) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}
		}

		// error handler params must be evaluated before the handled piece
		CatchHandler catchHandler = compiled.errorHandlers.get(piece);
		if(catchHandler != null) {
			buildPiece(catchHandler.handlerPiece, new HashSet<>(visited));
		}

		EnumSet<SpellParam.Side> usedSides = EnumSet.noneOf(SpellParam.Side.class);

		HashSet<SpellPiece> params = new HashSet<>();
		HashSet<SpellPiece> handledErrors = new HashSet<>();
		for(SpellParam<?> param : piece.paramSides.keySet()) {
			if(checkSideDisabled(param, piece, usedSides)) {
				continue;
			}

			SpellParam.Side side = piece.paramSides.get(param);

			SpellPiece pieceAt = compiled.sourceSpell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side, this::buildRedirect);

			if(pieceAt == null) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}
			if(!param.canAccept(pieceAt)) {
				throw new SpellCompilationException(SpellCompilationException.INVALID_PARAM, piece.x, piece.y);
			}

			if(piece instanceof IErrorCatcher && ((IErrorCatcher) piece).catchParam(param)) {
				handledErrors.add(pieceAt);
			} else {
				params.add(pieceAt);
			}
		}
		for(SpellPiece pieceAt : params) {
			HashSet<SpellPiece> visitedCopy = new HashSet<>(visited);
			// error handler params can't depend on handled pieces
			visitedCopy.addAll(handledErrors);
			buildPiece(pieceAt, visitedCopy);
		}
	}

	public void buildHandler(SpellPiece piece) throws SpellCompilationException {
		if(!(piece instanceof IErrorCatcher)) {
			return;
		}
		IErrorCatcher errorCatcher = (IErrorCatcher) piece;
		CompiledSpell.CatchHandler errorHandler = compiled.new CatchHandler(piece);

		EnumSet<SpellParam.Side> usedSides = EnumSet.noneOf(SpellParam.Side.class);

		for(SpellParam<?> param : piece.paramSides.keySet()) {
			if(!errorCatcher.catchParam(param) || checkSideDisabled(param, piece, usedSides)) {
				continue;
			}

			SpellParam.Side side = piece.paramSides.get(param);

			SpellPiece pieceAt = compiled.sourceSpell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side, this::buildRedirect);

			if(pieceAt == null) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}
			if(!param.canAccept(pieceAt)) {
				throw new SpellCompilationException(SpellCompilationException.INVALID_PARAM, piece.x, piece.y);
			}

			compiled.errorHandlers.put(pieceAt, errorHandler);
		}
	}

	public void buildRedirect(SpellPiece piece) throws SpellCompilationException {
		if(redirectionPieces.add(piece)) {
			try {
				piece.addToMetadata(compiled.metadata);
			} catch (NullPointerException e) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}

			EnumSet<SpellParam.Side> usedSides = EnumSet.noneOf(SpellParam.Side.class);

			for(SpellParam<?> param : piece.paramSides.keySet()) {
				checkSideDisabled(param, piece, usedSides);
			}
		}
	}

	/** @return whether this piece should get skipped over */
	private boolean checkSideDisabled(SpellParam<?> param, SpellPiece parent, EnumSet<SpellParam.Side> seen) throws SpellCompilationException {
		SpellParam.Side side = parent.paramSides.get(param);
		if(side.isEnabled()) {
			if(!seen.add(side)) {
				throw new SpellCompilationException(SpellCompilationException.SAME_SIDE_PARAMS, parent.x, parent.y);
			}
			return false;
		} else {
			if(!param.canDisable) {
				throw new SpellCompilationException(SpellCompilationException.UNSET_PARAM, parent.x, parent.y);
			}
			return true;
		}
	}

	public List<SpellPiece> findPieces(Predicate<EnumPieceType> match) throws SpellCompilationException {
		List<SpellPiece> results = new LinkedList<>();
		for(int i = 0; i < SpellGrid.GRID_SIZE; i++) {
			for(int j = 0; j < SpellGrid.GRID_SIZE; j++) {
				SpellPiece piece = compiled.sourceSpell.grid.gridData[j][i];
				if(piece != null && match.test(piece.getPieceType())) {
					results.add(0, piece);
				}

			}
		}

		return results;
	}

}
