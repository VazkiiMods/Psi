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
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.spell.trick.PieceTrickSaveVector;

public class PieceSelectorSavedVector extends PieceSelector {

    SpellParam<Number> number;

    public PieceSelectorSavedVector(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_NUMBER, true).mul(6));
    }

    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double numberVal = this.<Double>getParamEvaluation(number);
        if (numberVal == null || numberVal <= 0 || numberVal != numberVal.intValue()) {
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
        }

        meta.addStat(EnumSpellStat.POTENCY, numberVal.intValue() * 6);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        int numberVal = this.getParamValue(context, number).intValue();

        int n = numberVal - 1;
        if (context.customData.containsKey(PieceTrickSaveVector.KEY_SLOT_LOCKED + n)) {
            throw new SpellRuntimeException(SpellRuntimeException.LOCKED_MEMORY);
        }

        ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
        if (cadStack == null || !(cadStack.getItem() instanceof ICAD cad)) {
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        }
        return cad.getStoredVector(cadStack, n);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

}
