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

import java.util.List;

import dev.latvian.mods.rhino.util.ReturnsSelf;

/**
 * Operators and selectors: pieces that evaluate to a declared value type.
 */
@ReturnsSelf
public class OperatorPieceBuilder extends ScriptedPieceBuilder {

	private ScriptValueType returnType;
	private Boolean foldable;

	public OperatorPieceBuilder(ResourceLocation id, EnumPieceType pieceType) {
		super(id, pieceType);
	}

	public OperatorPieceBuilder returns(String type) {
		returnType = ScriptValueType.byScriptName(type);
		return this;
	}

	/**
	 * Whether the compiler may run the callback with constant inputs at compile time; a foldable
	 * callback receives a null context.
	 */
	public OperatorPieceBuilder foldable(boolean value) {
		foldable = value;
		return this;
	}

	@Override
	protected void collectMissing(List<String> missing) {
		super.collectMissing(missing);
		require(missing, "returns", returnType);
		require(missing, "foldable", foldable);
	}

	@Override
	protected ScriptValueType returnType() {
		return returnType;
	}

	@Override
	protected boolean foldable() {
		return foldable;
	}

}
