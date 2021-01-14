/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [25/01/2016, 20:16:02 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibPieceNames;

import java.util.stream.Collectors;

public class PieceTrickInfusion extends PieceTrick {
	public PieceTrickInfusion(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		addPotencyAndCost(meta);
	}

	protected void addPotencyAndCost(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 100);
		meta.addStat(EnumSpellStat.COST, 1200);
	}

	@Override
	public Object execute(SpellContext context) {
		for (TrickRecipe recipe :
				PsiAPI.trickRecipes.stream()
						.filter(recipe ->
								recipe.getPiece().isEmpty() || LibPieceNames.TRICK_INFUSION.equals(recipe.getPiece()))
						.collect(Collectors.toList())
			) {
				ItemCAD.craft(context.caster, recipe.getInput(), recipe.getOutput());
			}
		return null;
	}

}
