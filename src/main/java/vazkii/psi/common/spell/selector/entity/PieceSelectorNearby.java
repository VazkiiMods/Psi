/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;
import java.util.function.Predicate;

public abstract class PieceSelectorNearby extends PieceSelector {

	SpellParam<Vector3> position;
	SpellParam<Number> radius;

	public PieceSelectorNearby(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double radiusVal = this.<Double>getParamEvaluation(radius);
		if (radiusVal == null || radiusVal <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		double radiusVal = this.getParamValue(context, radius).doubleValue();

		if (!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);

		Predicate<Entity> pred = getTargetPredicate(context);

		List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> e != null && pred.test(e) && e != context.caster && e != context.focalPoint && context.isInRadius(e));

		return new EntityListWrapper(list);
	}

	public abstract Predicate<Entity> getTargetPredicate(SpellContext context);

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
