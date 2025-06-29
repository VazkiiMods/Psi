/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.constant;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;

public class PieceConstantWrapper extends SpellPiece {

    SpellParam<Number> target;
    SpellParam<Number> max;

    boolean evaluating = false;

    public PieceConstantWrapper(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.COMPLEXITY, new StatLabel(1));
    }

    @Override
    public void initParams() {
        addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(max = new ParamNumber("psi.spellparam.constant", SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double targetVal = this.getParamValue(context, target).doubleValue();
        double maxVal = this.getParamValue(context, max).doubleValue();

        if (maxVal > 0) {
            return Math.min(maxVal, Math.abs(targetVal));
        } else if (maxVal < 0) {
            return Math.max(maxVal, -Math.abs(targetVal));
        } else {
            return 0.0;
        }
    }

    @Override
    public Object evaluate() throws SpellCompilationException {
        if (evaluating) {
            return 0.0;
        }

        evaluating = true;
        Object ret = getParamEvaluation(max);
        evaluating = false;

        return ret;
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.CONSTANT;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

}
