/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.api.spell.*;
import vazkii.psi.common.block.BlockConjured;

public class PieceTrickConjureLight extends PieceTrickConjureBlock {

	public PieceTrickConjureLight(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(25));
		setStatLabel(EnumSpellStat.COST, new StatLabel(100));
	}

	@Override
	public void addStats(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.POTENCY, 25);
		meta.addStat(EnumSpellStat.COST, 100);
	}

	@Override
	public BlockState messWithState(BlockState state) {
		return state.setValue(BlockConjured.LIGHT, true);
	}

}
