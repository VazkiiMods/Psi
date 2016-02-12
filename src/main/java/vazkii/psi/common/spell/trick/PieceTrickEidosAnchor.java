/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/02/2016, 23:21:57 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class PieceTrickEidosAnchor extends PieceTrick {

	SpellParam time;

	public PieceTrickEidosAnchor(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double timeVal = this.<Double>getParamEvaluation(time);

		if(timeVal == null ||  timeVal <= 0 || timeVal.doubleValue() != timeVal.intValue())
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

		meta.addStat(EnumSpellStat.POTENCY, (int) (timeVal * 5.5 + 20));
		meta.addStat(EnumSpellStat.COST, timeVal.intValue() * 40);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double timeVal = this.<Double>getParamValue(context, time);
		PlayerData data = PlayerDataHandler.get(context.caster);
		data.eidosAnchor = Vector3.fromEntity(context.caster);
		data.eidosAnchorTime = timeVal.intValue() * 20;
		data.postAnchorRecallTime = 0;
		data.isAnchored = true;

		return null;
	}

}
