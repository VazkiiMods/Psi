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

import vazkii.psi.api.spell.SpellPieceType;

import java.util.List;

public class CraftingTrickPieceBuilder extends ScriptPieceBuilder {

	public CraftingTrickPieceBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected void collectMissing(List<String> missing) {}

	@Override
	protected SpellPieceType build(ScriptPieceStats stats) {
		return SpellPieceType.of(spell -> new ScriptCraftingTrick(spell, stats));
	}

}
