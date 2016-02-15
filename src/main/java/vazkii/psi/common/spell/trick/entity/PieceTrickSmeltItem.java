/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [15/02/2016, 18:11:08 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSmeltItem extends PieceTrick {

	SpellParam target;

	public PieceTrickSmeltItem(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 200);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);

		if(targetVal instanceof EntityItem) {
			EntityItem eitem = (EntityItem) targetVal;
			ItemStack stack = eitem.getEntityItem();
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
			
			if(result != null) {
				stack.stackSize--;
				if(stack.stackSize == 0 && !eitem.worldObj.isRemote)
					eitem.setDead();
				
				EntityItem item = new EntityItem(context.caster.worldObj, eitem.posX, eitem.posY, eitem.posZ, result.copy());
				if(!context.caster.worldObj.isRemote)
					context.caster.worldObj.spawnEntityInWorld(item);
			}
		} else throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

		return null;
	}

}
