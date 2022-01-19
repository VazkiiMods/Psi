/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbySmeltables;

public class PieceTrickSmeltItem extends PieceTrick {

	SpellParam<Entity> target;

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

		meta.addStat(EnumSpellStat.POTENCY, 80);
		meta.addStat(EnumSpellStat.COST, 240);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);

		if (targetVal instanceof ItemEntity && targetVal.isAlive()) {
			ItemEntity eitem = (ItemEntity) targetVal;
			ItemStack stack = eitem.getItem();
			ItemStack result = PieceSelectorNearbySmeltables.simulateSmelt(eitem.getCommandSenderWorld(), stack);

			if (!result.isEmpty()) {
				stack.shrink(1);
				eitem.setItem(stack);
				if (stack.getCount() == 0) {
					eitem.remove();
				}

				ItemEntity item = new ItemEntity(context.focalPoint.getCommandSenderWorld(), eitem.getX(), eitem.getY(), eitem.getZ(), result.copy());
				context.focalPoint.getCommandSenderWorld().addFreshEntity(item);
			}
		} else {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return null;
	}

}
