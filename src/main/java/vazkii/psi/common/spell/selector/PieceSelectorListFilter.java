package vazkii.psi.common.spell.selector;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceSelectorListFilter extends PieceSelector {

	SpellParam<EntityListWrapper> list;
	SpellParam<Number> number;

	public PieceSelectorListFilter(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST, SpellParam.CYAN, false, false));
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int num = this.getNonnullParamValue(context, number).intValue();
		EntityListWrapper listVal = this.getNonnullParamValue(context, list);

		if(num >= 0 && num < listVal.unwrap().size())
			return listVal.unwrap().get(num);
		else
			throw new SpellRuntimeException(SpellRuntimeException.OUT_OF_BOUNDS);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}
}
