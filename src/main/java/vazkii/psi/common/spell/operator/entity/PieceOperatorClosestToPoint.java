/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;

import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorClosestToPoint extends PieceOperator {

	SpellParam<Vector3> position;
	SpellParam<EntityListWrapper> list;

	public PieceOperatorClosestToPoint(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(list = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper listVal = this.getParamValue(context, list);
		Vector3 positionVal = this.getParamValue(context, position);

		return closestToPoint(positionVal, listVal);
	}

	public static Entity closestToPoint(Vector3 position, Iterable<Entity> list) throws SpellRuntimeException {
		double closest = Double.MAX_VALUE;
		Entity closestEntity = null;
		for (Entity e : list) {
			double dist = MathHelper.pointDistanceSpace(position.x, position.y, position.z, e.getX(), e.getY(), e.getZ());
			if (dist < closest) {
				closest = dist;
				closestEntity = e;
			}
		}

		if (closestEntity == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return closestEntity;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
