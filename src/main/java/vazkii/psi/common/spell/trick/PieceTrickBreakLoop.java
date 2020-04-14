package vazkii.psi.common.spell.trick;

import net.minecraft.nbt.CompoundNBT;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellCircle;

public class PieceTrickBreakLoop extends PieceTrick {

	private SpellParam<Number> valueParam;

	public PieceTrickBreakLoop(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(valueParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		// NO-OP
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double value = this.getParamValue(context, valueParam).doubleValue();

		if (Math.abs(value) < 1.0) {
			if (context.focalPoint != context.caster) {
				if (context.focalPoint instanceof EntitySpellCircle) {
					EntitySpellCircle circle = (EntitySpellCircle) context.focalPoint;
					CompoundNBT circleNBT = new CompoundNBT();
					circle.writeAdditional(circleNBT);
					circleNBT.putInt("timesCast", 20);
					circleNBT.putInt("timesAlive", 100);
					circle.read(circleNBT);
				} else
					context.focalPoint.remove();
			} else {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
				data.stopLoopcast();
			}

			context.stopped = true;
		}
		return null;
	}
}
