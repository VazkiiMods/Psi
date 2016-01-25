/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [25/01/2016, 20:16:02 (GMT)]
 */
package vazkii.psi.common.spell.trick;

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

public class PieceTrickInfusion extends PieceTrick {

	public PieceTrickInfusion(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		
		meta.addStat(EnumSpellStat.POTENCY, 100);
		meta.addStat(EnumSpellStat.COST, 1200);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		ItemCAD.craft(context.caster, new ItemStack(Items.gold_ingot), new ItemStack(ModItems.material, 1, 1));
		return null;
	}
	
}
