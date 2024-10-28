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
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorItemPresence extends PieceSelector {

    SpellParam<Number> slot;

    public PieceSelectorItemPresence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(slot = new ParamNumber("psi.spellparam.slot", SpellParam.BLUE, true, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Number slotVal = this.getParamValue(context, slot);
        int invSlot = (slotVal == null ? context.getTargetSlot() : Math.abs(slotVal.intValue() - 1)) % context.caster.getInventory().items.size();
        ItemStack stack = context.caster.getInventory().getItem(invSlot);

        return (double) stack.getCount();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

}
