/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [11/03/2016, 20:03:51 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

public class PieceSelectorEidosChangelog extends PieceSelector {

	SpellParam number;

	public PieceSelectorEidosChangelog(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double numberVal = this.<Double>getParamValue(context, number);
		PlayerData data = PlayerDataHandler.get(context.caster);
		
		int i = numberVal.intValue();
		if(i <= 0 || i >= data.eidosChangelog.size())
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		
		Vector3 vec = data.eidosChangelog.get(data.eidosChangelog.size() - i);
		if(vec == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		
		return vec;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
