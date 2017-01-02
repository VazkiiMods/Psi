/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [10/03/2016, 19:42:56 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorItemPresence extends PieceSelector {

	SpellParam slot;

	public PieceSelectorItemPresence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(slot = new ParamNumber("psi.spellparam.slot", SpellParam.BLUE, true, true));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double slotVal = this.<Double>getParamValue(context, slot);
		int invSlot = (slotVal == null ? context.getTargetSlot() : Math.abs(slotVal.intValue() - 1)) % context.caster.inventory.mainInventory.size();
		ItemStack stack = context.caster.inventory.getStackInSlot(invSlot);
		
		return stack == null ? 0.0 : Double.valueOf(stack.getCount());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
