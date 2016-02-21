/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/02/2016, 18:44:56 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.state.IBlockState;
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
		meta.addStat(EnumSpellStat.POTENCY, 50);
		meta.addStat(EnumSpellStat.COST, 200);
	}

	@Override
	public IBlockState messWithState(IBlockState state) {
		return state.withProperty(BlockConjured.LIGHT, true);
	}

}
