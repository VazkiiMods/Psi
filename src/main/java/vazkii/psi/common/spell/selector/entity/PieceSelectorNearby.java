/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 00:02:50 (GMT)]
 */
package vazkii.psi.common.spell.selector.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;
import java.util.function.Predicate;

public abstract class PieceSelectorNearby extends PieceSelector {

	SpellParam<Vector3> position;
	SpellParam<Number> radius;

	//TODO add this in the next version

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
		if (radiusVal == null || radiusVal <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		double radiusVal = this.getParamValue(context, radius).doubleValue();

		Vec3d positionCenter = context.focalPoint.getPositionVector();

		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal,
			positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);
		AxisAlignedBB eris = new AxisAlignedBB(positionCenter.x - SpellContext.MAX_DISTANCE, positionCenter.y - SpellContext.MAX_DISTANCE, positionCenter.z - SpellContext.MAX_DISTANCE,
			positionCenter.x + SpellContext.MAX_DISTANCE, positionCenter.y + SpellContext.MAX_DISTANCE, positionCenter.z + SpellContext.MAX_DISTANCE);
		AxisAlignedBB area = axis.intersect(eris);

		Predicate<Entity> pred = getTargetPredicate(context);

		List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, area, (Entity e) -> e != null && pred.test(e) && e != context.caster && e != context.focalPoint && context.isInRadius(e));

		return new EntityListWrapper(list);
	}

	public abstract Predicate<Entity> getTargetPredicate(SpellContext context);

	@Override
	public Class<?> getEvaluationType() {
		return EntityListWrapper.class;
	}

}
