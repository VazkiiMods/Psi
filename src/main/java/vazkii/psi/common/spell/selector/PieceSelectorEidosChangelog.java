/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
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

	SpellParam<Number> number;

	public PieceSelectorEidosChangelog(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int i = this.getParamValue(context, number).intValue();
		PlayerData data = PlayerDataHandler.get(context.caster);

		if(i <= 0 || i > data.eidosChangelog.size()) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		Vector3 vec = data.eidosChangelog.get(data.eidosChangelog.size() - i);
		if(vec == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		return vec;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
