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

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.CompiledSpell.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/* Probably not thread-safe. */
public final class SpellCompiler implements ISpellCompiler {

	/** The current spell being compiled. */
	private CompiledSpell compiled;

	private final Set<SpellPiece> processedHandlers = new HashSet<>();

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
		if (spell == null) {
			throw new SpellCompilationException(SpellCompilationException.NO_SPELL);
		}

		processedHandlers.clear();
		redirectionPieces.clear();
		compiled = new CompiledSpell(spell);

		List<SpellPiece> tricks = findPieces(EnumPieceType::isTrick);
		if (tricks.isEmpty()) {
			throw new SpellCompilationException(SpellCompilationException.NO_TRICKS);
		}
		for (SpellPiece trick : tricks) {
			buildPiece(trick);
		}

		for (SpellPiece piece : findPieces(Predicate.isEqual(EnumPieceType.ERROR_HANDLER))) {
			if (!processedHandlers.contains(piece)) {
				buildHandler(piece);
			}
		}

		if (compiled.metadata.stats.get(EnumSpellStat.COST) < 0 || compiled.metadata.stats.get(EnumSpellStat.POTENCY) < 0) {
			throw new SpellCompilationException(SpellCompilationException.STAT_OVERFLOW);
		}

		if (spell.name == null || spell.name.isEmpty()) {
			throw new SpellCompilationException(SpellCompilationException.NO_NAME);
		}
		return compiled;
	}

	public void buildPiece(SpellPiece piece) throws SpellCompilationException {
		buildPiece(piece, new HashSet<>());
	}

	public void buildPiece(SpellPiece piece, Set<SpellPiece> visited) throws SpellCompilationException {
		if (visited.add(piece)) {
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP, piece.x, piece.y);
		}

		if (compiled.actionMap.containsKey(piece)) { // move to top
			Action a = compiled.actionMap.get(piece);
			compiled.actions.remove(a);
			compiled.actions.add(a);
		} else {
			Action a = compiled.new Action(piece);
			compiled.actions.add(a);
			compiled.actionMap.put(piece, a);
			piece.addToMetadata(compiled.metadata);
		}

		CompiledSpell.CatchHandler errorHandler = null;
		if (piece instanceof IErrorCatcher) {
			errorHandler = compiled.new CatchHandler(piece);
			processedHandlers.add(piece);
		}

		List<SpellParam.Side> usedSides = new ArrayList<>();

		for (SpellParam<?> param : piece.paramSides.keySet()) {
			SpellParam.Side side = piece.paramSides.get(param);
			if (!side.isEnabled()) {
				if (!param.canDisable) {
					throw new SpellCompilationException(SpellCompilationException.UNSET_PARAM, piece.x, piece.y);
				}

				continue;
			}

			if (usedSides.contains(side)) {
				throw new SpellCompilationException(SpellCompilationException.SAME_SIDE_PARAMS, piece.x, piece.y);
			}
			usedSides.add(side);

			SpellPiece pieceAt = compiled.sourceSpell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side, this::buildRedirect);

			if (pieceAt == null) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}
			if (!param.canAccept(pieceAt)) {
				throw new SpellCompilationException(SpellCompilationException.INVALID_PARAM, piece.x, piece.y);
			}

			if (errorHandler != null) {
				compiled.errorHandlers.putIfAbsent(pieceAt, errorHandler);
			}

			buildPiece(pieceAt, new HashSet<>(visited));
		}
	}

	public void buildHandler(SpellPiece piece) throws SpellCompilationException {
		if (!(piece instanceof IErrorCatcher)) {
			return;
		}

		CompiledSpell.CatchHandler errorHandler = compiled.new CatchHandler(piece);

		piece.addToMetadata(compiled.metadata);

		List<SpellParam.Side> usedSides = new ArrayList<>();

		for (SpellParam<?> param : piece.paramSides.keySet()) {
			SpellParam.Side side = piece.paramSides.get(param);
			if (!side.isEnabled()) {
				if (!param.canDisable) {
					throw new SpellCompilationException(SpellCompilationException.UNSET_PARAM, piece.x, piece.y);
				}

				continue;
			}

			if (usedSides.contains(side)) {
				throw new SpellCompilationException(SpellCompilationException.SAME_SIDE_PARAMS, piece.x, piece.y);
			}
			usedSides.add(side);

			SpellPiece pieceAt = compiled.sourceSpell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side, this::buildRedirect);
			if (pieceAt == null) {
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			}
			if (!param.canAccept(pieceAt)) {
				throw new SpellCompilationException(SpellCompilationException.INVALID_PARAM, piece.x, piece.y);
			}

			if (((IErrorCatcher) piece).catchParam(param)) {
				compiled.errorHandlers.putIfAbsent(pieceAt, errorHandler);
			} else {
				buildPiece(pieceAt);
			}
		}
	}

	public void buildRedirect(SpellPiece piece) throws SpellCompilationException {
		if (!redirectionPieces.contains(piece)) {
			piece.addToMetadata(compiled.metadata);

			redirectionPieces.add(piece);

			List<SpellParam.Side> usedSides = new ArrayList<>();

			for (SpellParam<?> param : piece.paramSides.keySet()) {
				SpellParam.Side side = piece.paramSides.get(param);
				if (!side.isEnabled()) {
					if (!param.canDisable) {
						throw new SpellCompilationException(SpellCompilationException.UNSET_PARAM, piece.x, piece.y);
					}

					continue;
				}

				if (usedSides.contains(side)) {
					throw new SpellCompilationException(SpellCompilationException.SAME_SIDE_PARAMS, piece.x, piece.y);
				}
				usedSides.add(side);
			}
		}
	}

	public List<SpellPiece> findPieces(Predicate<EnumPieceType> match) throws SpellCompilationException {
		List<SpellPiece> results = new LinkedList<>();
		for (int i = 0; i < SpellGrid.GRID_SIZE; i++) {
			for (int j = 0; j < SpellGrid.GRID_SIZE; j++) {
				SpellPiece piece = compiled.sourceSpell.grid.gridData[j][i];
				if (piece != null && match.test(piece.getPieceType())) {
					results.add(0, piece);
				}

			}
		}

		return results;
	}

}
