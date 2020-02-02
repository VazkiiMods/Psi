/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [29/01/2016, 17:04:45 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageBlink;

public class PieceTrickBlink extends PieceTrick {

	SpellParam target;
	SpellParam distance;

	public PieceTrickBlink(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double distanceVal = this.<Double>getParamEvaluation(distance);
		if(distanceVal == null)
			distanceVal = 1D;

		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 30));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 40));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		Double distanceVal = this.<Double>getParamValue(context, distance);

		blink(context, targetVal, distanceVal);

		return null;
	}

	public static void blink(SpellContext context, Entity e, double dist) throws SpellRuntimeException {
        context.verifyEntity(e);
        if (!context.isInRadius(e))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        Vec3d look = e.getLookVec();

        double offX = look.x * dist;
        double offY = Math.max(0, look.y * dist);
        double offZ = look.z * dist;

        e.setPosition(e.getX() + offX, e.getY() + offY, e.getZ() + offZ);
        if (e instanceof ServerPlayerEntity)
            MessageRegister.HANDLER.sendToPlayer(new MessageBlink(offX, offY, offZ), (ServerPlayerEntity) e);
    }

}
