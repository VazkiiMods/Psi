/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.util.function.Predicate;

public class PieceOperatorEntityRaycast extends PieceOperator {

	SpellParam<Vector3> origin;
	SpellParam<Vector3> ray;
	SpellParam<Number> max;

	public PieceOperatorEntityRaycast(Spell spell) {
		super(spell);
	}

	public static Entity rayTraceEntities(Level world, Vec3 positionVector, Vec3 lookVector, Predicate<Entity> predicate, double maxDistance) {
		if(maxDistance < 0) {
			return null;
		}

		double rayLength = lookVector.length();
		double scale = rayLength > 1 ? maxDistance / rayLength : maxDistance;
		Vec3 reachVector = positionVector.add(lookVector.scale(scale));
		AABB aabb = new AABB(positionVector.x, positionVector.y, positionVector.z, reachVector.x, reachVector.y, reachVector.z).inflate(1f, 1f, 1f);
		EntityHitResult hit = ProjectileUtil.getEntityHitResult(world, null, positionVector, reachVector, aabb, predicate, 0);
		return hit == null ? null : hit.getEntity();
	}

	@Override
	public void initParams() {
		addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(ray = new ParamVector(SpellParam.GENERIC_NAME_RAY, SpellParam.GREEN, false, false));
		addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 originVal = this.getParamValue(context, origin);
		Vector3 rayVal = this.getParamValue(context, ray);

		if(originVal == null || rayVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		double maxLen = SpellHelpers.rangeLimitParam(this, context, max, SpellContext.MAX_DISTANCE);

		Entity entity = rayTraceEntities(context.focalPoint.level(), originVal.toVec3D(), rayVal.toVec3D(),
				pred -> !pred.isSpectator() && pred.isAlive() && pred.isPickable() && !(pred instanceof ISpellImmune), maxLen);
		if(entity == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return entity;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
