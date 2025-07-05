/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import static vazkii.psi.common.spell.operator.entity.PieceOperatorClosestToPoint.closestToPoint;

public class PieceOperatorClosestToLine extends PieceOperator {
	SpellParam<Vector3> rayStartParam;
	SpellParam<Vector3> rayEndParam;
	SpellParam<EntityListWrapper> entList;

	public PieceOperatorClosestToLine(Spell spell) {
		super(spell);
	}

	public static Entity closestToLineSegment(Vector3 a, Vector3 b, Iterable<Entity> list) throws SpellRuntimeException {
		if(a.equals(b)) {
			return closestToPoint(a, list);
		}
		Vec3 start = a.toVec3D();
		Vec3 end = b.toVec3D();
		Vec3 diff = end.subtract(start).normalize();
		double minDot = diff.dot(start);
		double maxDot = diff.dot(end);

		double minDist = Double.MAX_VALUE;
		Entity found = null;

		for(Entity e : list) {
			Vec3 pos = e.position();
			double dot = diff.dot(pos);
			double dist;
			if(dot <= minDot) {
				dist = pos.subtract(start).length();
			} else if(dot >= maxDot) {
				dist = pos.subtract(end).length();
			} else {
				dist = pos.subtract(start).cross(diff).length();
			}

			if(dist < minDist) {
				minDist = dist;
				found = e;
			}
		}
		if(found == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		return found;
	}

	@Override
	public void initParams() {
		super.initParams();
		addParam(rayStartParam = new ParamVector(SpellParam.GENERIC_NAME_RAY_START, SpellParam.RED, false, false));
		addParam(rayEndParam = new ParamVector(SpellParam.GENERIC_NAME_RAY_END, SpellParam.BLUE, false, false));
		addParam(entList = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST, SpellParam.GREEN, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 rayStart = SpellHelpers.getVector3(this, context, rayStartParam, false, false);
		Vector3 rayEnd = SpellHelpers.getVector3(this, context, rayEndParam, false, false);
		EntityListWrapper list = this.getNotNullParamValue(context, entList);
		if(list.size() == 0) {
			return null;
		}

		return closestToLineSegment(rayStart, rayEnd, list);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}
}
