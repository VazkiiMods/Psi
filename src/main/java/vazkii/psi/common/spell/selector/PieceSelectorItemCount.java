package vazkii.psi.common.spell.selector;

import net.minecraft.item.ItemStack;

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
		ItemStack toCount = context.caster.inventory.mainInventory.get(context.getTargetSlot());
		return context.caster.inventory.mainInventory.stream().filter(stack -> stack.isItemEqual(toCount)).mapToInt(ItemStack::getCount).reduce(Integer::sum).orElse(0);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
