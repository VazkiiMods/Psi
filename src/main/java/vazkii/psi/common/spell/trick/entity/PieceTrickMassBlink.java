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
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceTrickMassBlink extends PieceTrick {

    SpellParam<EntityListWrapper> target;
    SpellParam<Number> distance;

    public PieceTrickMassBlink(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_DISTANCE, true).abs().mul(90));
        setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_DISTANCE, true).abs().mul(105).max(1));
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        Double distanceVal = this.<Double>getParamEvaluation(distance);
        if (distanceVal == null) {
            distanceVal = 1D;
        }

        meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 80));
        meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 100));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper targetVal = this.getParamValue(context, target);
        double distanceVal = this.getParamValue(context, distance).doubleValue();

        for (Entity e : targetVal) {
            PieceTrickBlink.blink(context, e, distanceVal);
        }

        return null;
    }

}
