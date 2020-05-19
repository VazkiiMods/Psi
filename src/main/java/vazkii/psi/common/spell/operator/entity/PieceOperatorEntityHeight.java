package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityHeight extends PieceOperator {

	SpellParam<Entity> target;

	public PieceOperatorEntityHeight(Spell spell) {
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

		return e.getHeight();
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
