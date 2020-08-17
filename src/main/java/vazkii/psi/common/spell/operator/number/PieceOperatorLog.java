/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorLog extends PieceOperator {

	SpellParam<Number> num;
	SpellParam<Number> base;

	public PieceOperatorLog(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
		addParam(base = new ParamNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.RED, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d = this.getParamValue(context, num).doubleValue();

		if (d < 0) {
			throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		}

		double logNum = Math.log10(d);

		Number b = this.getParamValue(context, base);
		if (b != null) {
			if (b.doubleValue() < 0) {
				throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
			}

			logNum /= Math.log10(b.doubleValue());
		}

		return logNum;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
