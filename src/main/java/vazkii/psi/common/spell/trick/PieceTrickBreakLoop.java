/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.nbt.CompoundTag;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellCircle;

public class PieceTrickBreakLoop extends PieceTrick {

	SpellParam<Number> valueParam;

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
					CompoundTag circleNBT = new CompoundTag();
					circle.addAdditionalSaveData(circleNBT);
					circleNBT.putInt("timesCast", 20);
					circleNBT.putInt("timesAlive", 100);
					circle.load(circleNBT);
				} else {
					context.focalPoint.remove();
				}
			} else {
				if (!context.tool.isEmpty() && context.tool.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).isPresent()) {
					ISocketable socketableCap = context.tool.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);
					socketableCap.setSelectedSlot(socketableCap.getLastSlot() + 1);
				}
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
				data.stopLoopcast();
			}
		}
		return null;
	}
}
