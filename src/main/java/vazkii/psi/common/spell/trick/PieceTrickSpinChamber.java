/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.Objects;

public class PieceTrickSpinChamber extends PieceTrick {
    private SpellParam<Number> number;

    public PieceTrickSpinChamber(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.POTENCY, new StatLabel(2));
    }

    public static int getNextSlotFromOffset(ISocketable socketable, int offset) {
        int currentSlot = socketable.getSelectedSlot();
        if (offset > 0) {
            return socketable.isSocketSlotAvailable(currentSlot + 1) ? currentSlot + 1 : 0;
        }
        if (socketable.isSocketSlotAvailable(currentSlot - 1)) {
            return currentSlot - 1;
        }
        return socketable.getLastSlot();
    }

    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double num = this.getParamValue(context, number).doubleValue();

        if (num == 0) {
            return null;
        }

        ItemStack stack = context.tool.isEmpty() ? PsiAPI.getPlayerCAD(context.caster) : context.tool;
        boolean updateLoopcast = (stack.getItem() instanceof ICAD) && (context.castFrom == PlayerDataHandler.get(context.caster).loopcastHand);
        ISocketable capability = Objects.requireNonNull(stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY));
        int offset = num > 0 ? 1 : -1;
        int targetSlot = getNextSlotFromOffset(capability, offset);

        capability.setSelectedSlot(targetSlot);

        if (updateLoopcast) {
            PlayerDataHandler.get(context.caster).lastTickLoopcastStack = stack.copy();
        }
        return null;
    }
}
