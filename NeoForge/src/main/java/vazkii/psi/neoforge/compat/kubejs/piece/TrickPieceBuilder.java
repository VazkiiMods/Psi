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

public class TrickPieceBuilder extends ScriptedPieceBuilder {

	public TrickPieceBuilder(ResourceLocation id) {
		super(id, EnumPieceType.TRICK);
	}

	@Override
	protected ScriptValueType returnType() {
		return ScriptValueType.ANY;
	}

	@Override
	protected boolean foldable() {
		return false;
	}

}
