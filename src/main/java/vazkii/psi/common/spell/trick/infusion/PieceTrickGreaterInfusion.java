/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 19:33:34 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

public class PieceTrickGreaterInfusion extends PieceTrick {

	public PieceTrickGreaterInfusion(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 250);
		meta.addStat(EnumSpellStat.COST, 2600);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		ItemCAD.craft(context.caster, new ItemStack(Items.diamond), new ItemStack(ModItems.material, 1, 2));
		return null;
	}

}
