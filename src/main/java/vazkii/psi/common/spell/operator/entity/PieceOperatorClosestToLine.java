/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;
import vazkii.psi.common.core.helpers.SpellHelpers;

import static vazkii.psi.common.spell.operator.entity.PieceOperatorClosestToPoint.closestToPoint;

public class PieceOperatorClosestToLine extends PieceOperator {
	SpellParam<Vector3> rayStartParam;
	SpellParam<Vector3> rayEndParam;
	SpellParam<EntityListWrapper> entList;

	public PieceOperatorClosestToLine(Spell spell) {
		super(spell);
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
		EntityListWrapper list = this.getNonnullParamValue(context, entList);
		if (list.size() == 0) {
			return null;
		}

		return closestToLineSegment(rayStart, rayEnd, list);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

	public static Entity closestToLineSegment(Vector3 a, Vector3 b, Iterable<Entity> list) throws SpellRuntimeException {
		if (a.equals(b)) {
			return closestToPoint(a, list);
		}
		Vector3d start = a.toVec3D();
		Vector3d end = b.toVec3D();
		Vector3d diff = end.subtract(start).normalize();
		double minDot = diff.dotProduct(start);
		double maxDot = diff.dotProduct(end);

		double minDist = Double.MAX_VALUE;
		Entity found = null;

		for (Entity e : list) {
			Vector3d pos = e.getPositionVec();
			double dot = diff.dotProduct(pos);
			double dist;
			if (dot <= minDot) {
				dist = pos.subtract(start).length();
			} else if (dot >= maxDot) {
				dist = pos.subtract(end).length();
			} else {
				dist = pos.subtract(start).crossProduct(diff).length();
			}

			if (dist < minDist) {
				minDist = dist;
				found = e;
			}
		}
		if (found == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		return found;
	}
}
