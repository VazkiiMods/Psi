/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;

public class PieceOperatorEntityMotion extends PieceOperator {

    SpellParam<Entity> target;

    public PieceOperatorEntityMotion(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity e = this.getParamValue(context, target);

        if (e == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        }

        if (e instanceof Player player) {
            PlayerData data = PlayerDataHandler.get(player);
            if (data.eidosChangelog.size() >= 2) {
                Vector3 last = data.eidosChangelog.get(data.eidosChangelog.size() - 2);
                Vector3 vec = Vector3.fromEntity(e).sub(last).multiply(1.0 / PieceTrickAddMotion.MULTIPLIER);
                if (vec.mag() < 10) {
                    return vec;
                }
            }
        }

        return new Vector3(e.getDeltaMovement()).multiply(1.0 / PieceTrickAddMotion.MULTIPLIER);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

}
