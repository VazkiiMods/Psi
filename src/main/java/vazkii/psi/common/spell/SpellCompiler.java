/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/01/2016, 14:48:11 (GMT)]
 */
package vazkii.psi.common.spell;

import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.CompiledSpell.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class SpellCompiler implements ISpellCompiler {

	final Spell spell;
	CompiledSpell compiled = null;

	String error = null;
	Pair<Integer, Integer> errorLocation = null;

	final Stack<SpellPiece> tricks = new Stack<>();

	public SpellCompiler(Spell spell) {
		this.spell = spell;

		try {
			compile();
		} catch(SpellCompilationException e) {
			error = e.getMessage();
			errorLocation = e.location;
		}
	}

	public void compile() throws SpellCompilationException {
		if(spell == null)
			throw new SpellCompilationException(SpellCompilationException.NO_SPELL);

		compiled = new CompiledSpell(spell);
		findTricks();

		while(!tricks.isEmpty())
			buildPiece(tricks.pop());

		if(compiled.metadata.stats.get(EnumSpellStat.COST) < 0 || compiled.metadata.stats.get(EnumSpellStat.POTENCY) < 0)
			throw new SpellCompilationException(SpellCompilationException.STAT_OVERFLOW);

		if(spell.name == null || spell.name.isEmpty())
			throw new SpellCompilationException(SpellCompilationException.NO_NAME);
	}

	public void buildPiece(SpellPiece piece) throws SpellCompilationException {
		buildPiece(piece, new ArrayList<>());
	}

	public void buildPiece(SpellPiece piece, List<SpellPiece> visited) throws SpellCompilationException {
		if(visited.contains(piece))
			throw new SpellCompilationException(SpellCompilationException.INFINITE_LOOP, piece.x, piece.y);

		if(compiled.actionMap.containsKey(piece)) { // move to top
			Action a = compiled.actionMap.get(piece);
			compiled.actions.remove(a);
			compiled.actions.add(a);
		} else {
			Action a = compiled.new Action(piece);
			compiled.actions.add(a);
			compiled.actionMap.put(piece, a);
			piece.addToMetadata(compiled.metadata);
		}

		visited.add(piece);

		List<SpellParam.Side> usedSides = new ArrayList<>();

		for(SpellParam param : piece.paramSides.keySet()) {
			SpellParam.Side side = piece.paramSides.get(param);
			if(!side.isEnabled()) {
				if(!param.canDisable)
					throw new SpellCompilationException(SpellCompilationException.UNSET_PARAM, piece.x, piece.y);

				continue;
			}

			if(usedSides.contains(side))
				throw new SpellCompilationException(SpellCompilationException.SAME_SIDE_PARAMS, piece.x, piece.y);
			usedSides.add(side);

			SpellPiece pieceAt = spell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side);
			if(pieceAt == null)
				throw new SpellCompilationException(SpellCompilationException.NULL_PARAM, piece.x, piece.y);
			if(!param.canAccept(pieceAt))
				throw new SpellCompilationException(SpellCompilationException.INVALID_PARAM, piece.x, piece.y);

			buildPiece(pieceAt, new ArrayList<>(visited));
		}
	}

	public void findTricks() throws SpellCompilationException {
		for(int i = 0; i < SpellGrid.GRID_SIZE; i++)
			for(int j = 0; j < SpellGrid.GRID_SIZE; j++) {
				SpellPiece piece = spell.grid.gridData[j][i];
				if(piece != null) {
					if(piece.getPieceType() == EnumPieceType.TRICK)
						tricks.add(piece);
					else if(piece.getPieceType() == EnumPieceType.MODIFIER)
						piece.addToMetadata(compiled.metadata);
				}

			}

		if(tricks.isEmpty())
			throw new SpellCompilationException(SpellCompilationException.NO_TRICKS);
	}

	@Override
	public CompiledSpell getCompiledSpell() {
		return compiled;
	}

	@Override
	public String getError() {
		return error;
	}

	@Override
	public Pair<Integer, Integer> getErrorLocation() {
		return errorLocation;
	}

	@Override
	public boolean isErrored() {
		return error != null;
	}

}
