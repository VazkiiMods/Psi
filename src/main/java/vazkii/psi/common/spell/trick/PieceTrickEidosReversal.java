/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.server.level.ServerPlayer;

import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageEidosSync;

public class PieceTrickEidosReversal extends PieceTrick {

	SpellParam<Number> time;

	public PieceTrickEidosReversal(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).mul(11).add(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_TIME, true).mul(40));
	}

	@Override
	public void initParams() {
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		double timeVal = this.<Number, IntervalNumber>getNonNullParamEvaluation(time).max;
		
		if(timeVal < 1) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, (int) (timeVal * 11 + 20));
		meta.addStat(EnumSpellStat.COST, ((int) timeVal) * 40);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int timeVal = this.getParamValue(context, time).intValue();
		PlayerData data = PlayerDataHandler.get(context.caster);
		if(timeVal > 0 && !data.isReverting) {
			data.eidosReversionTime = timeVal * 10;
			data.isReverting = true;
			if(context.caster instanceof ServerPlayer) {
				MessageRegister.sendToPlayer(new MessageEidosSync(data.eidosReversionTime), context.caster);
			}
		}

		return null;
	}

}
