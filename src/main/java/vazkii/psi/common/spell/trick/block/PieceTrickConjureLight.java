/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.BlockState;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.common.block.BlockConjured;

public class PieceTrickConjureLight extends PieceTrickConjureBlock {

	public PieceTrickConjureLight(Spell spell) {
		super(spell);
	}

	@Override
	public void addStats(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 25);
		meta.addStat(EnumSpellStat.COST, 100);
	}

	@Override
	public BlockState messWithState(BlockState state) {
		return state.with(BlockConjured.LIGHT, true);
	}

}
