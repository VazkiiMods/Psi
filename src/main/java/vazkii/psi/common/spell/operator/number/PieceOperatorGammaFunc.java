package vazkii.psi.common.spell.operator.number;

import vazkii.psi.api.internal.math.Gamma;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGammaFunc extends PieceOperator {

	SpellParam<Number> num1;

	public PieceOperatorGammaFunc(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(num1 = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double d1 = this.getParamValue(context, num1).doubleValue();
		return Gamma.gamma(d1);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
