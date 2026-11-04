/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import net.minecraft.resources.ResourceLocation;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.SpellPieceType;

import java.util.ArrayList;
import java.util.List;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.rhino.util.ReturnsSelf;

/**
 * A piece kind whose behaviour is a script callback reading declared params.
 */
@ReturnsSelf
public abstract class ScriptedPieceBuilder extends ScriptPieceBuilder {

	private final EnumPieceType pieceType;
	private final List<ScriptPieceParam> params = new ArrayList<>();
	private boolean paramsDeclared;
	private ScriptPieceBody body;

	protected ScriptedPieceBuilder(ResourceLocation id, EnumPieceType pieceType) {
		super(id);
		this.pieceType = pieceType;
	}

	/**
	 * Declares an input. Call {@link #noParams()} instead for a piece with none.
	 */
	public ScriptedPieceBuilder param(String name, String type, boolean canDisable) {
		if(params.stream().anyMatch(param -> param.name().equals(name))) {
			throw new KubeRuntimeException("Spell piece '" + id + "' declares param '" + name + "' twice").source(sourceLine);
		}
		params.add(new ScriptPieceParam(name, ScriptValueType.byScriptName(type), canDisable));
		paramsDeclared = true;
		return this;
	}

	public ScriptedPieceBuilder noParams() {
		paramsDeclared = true;
		return this;
	}

	public ScriptedPieceBuilder execute(ScriptPieceBody body) {
		this.body = body;
		return this;
	}

	@Override
	protected void collectMissing(List<String> missing) {
		if(!paramsDeclared) {
			missing.add("param(...) or noParams()");
		}
		require(missing, "execute", body);
	}

	@Override
	protected final SpellPieceType build(ScriptPieceStats stats) {
		ScriptSpellPiece.Definition definition = new ScriptSpellPiece.Definition(pieceType, returnType(), List.copyOf(params), stats, foldable(), body);
		return SpellPieceType.of(spell -> new ScriptSpellPiece(spell, definition));
	}

	protected abstract ScriptValueType returnType();

	protected abstract boolean foldable();

}
