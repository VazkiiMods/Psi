/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs.piece;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;

/**
 * A crafting trick whose behaviour is entirely the trick recipes naming its id.
 */
public final class ScriptCraftingTrick extends PieceCraftingTrick {

	private final ScriptPieceStats stats;

	public ScriptCraftingTrick(Spell spell, ScriptPieceStats stats) {
		super(spell);
		this.stats = stats;
		stats.label(this);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		stats.addTo(meta);
	}

	@Override
	public boolean canCraft(PieceCraftingTrick trick) {
		return getRegistryKey().equals(trick.getRegistryKey());
	}

}
