/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.world.item.ItemStack;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorItemCount extends PieceSelector {

	public PieceSelectorItemCount(Spell spell) {
		super(spell);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		ItemStack toCount = context.caster.getInventory().items.get(context.getTargetSlot());
		return context.caster.getInventory().items.stream().filter(stack -> stack.sameItem(toCount)).mapToInt(ItemStack::getCount).reduce(Integer::sum).orElse(0);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
