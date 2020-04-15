package vazkii.psi.common.spell.operator.list;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class PieceOperatorListUnion extends PieceOperator {

	SpellParam<EntityListWrapper> list1;
	SpellParam<EntityListWrapper> list2;

	public PieceOperatorListUnion(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(list1 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
		addParam(list2 = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper l1 = this.getNonnullParamValue(context, list1);
		EntityListWrapper l2 = this.getNonnullParamValue(context, list2);

		List<Entity> entities = new ArrayList<>(l1.unwrap());
		entities.addAll(l2.unwrap());
		entities = new ArrayList<>(new LinkedHashSet<>(entities));
		return new EntityListWrapper(entities);
	}

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}
}
