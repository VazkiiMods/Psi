/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 19:37:26 (GMT)]
 */
package vazkii.psi.common.spell.trick.infusion;

import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.dimension.EndDimension;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

public class PieceTrickEbonyIvory extends PieceTrickGreaterInfusion {
	public PieceTrickEbonyIvory(Spell spell) {
		super(spell);
	}

	@Override
	protected void addPotencyAndCost(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 250);
		meta.addStat(EnumSpellStat.COST, 3000);
	}

	@Override
	public Object execute(SpellContext context) {
		super.execute(context);
		if(context.caster.getEntityWorld().getDimension() instanceof EndDimension) {
			ItemCAD.craft(context.caster, new ItemStack(Items.COAL), new ItemStack(ModItems.material, 1, 5));
			ItemCAD.craft(context.caster, "gemQuartz", new ItemStack(ModItems.material, 1, 6));
		}

		return null;
	}

}
