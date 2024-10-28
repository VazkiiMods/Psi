/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickEvaluate extends PieceTrick {

    SpellParam<SpellParam.Any> target;

    public PieceTrickEvaluate(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) {
        return null;
    }

}
