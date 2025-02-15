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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.util.Optional;
import java.util.function.Predicate;

public class PieceOperatorEntityRaycast extends PieceOperator {

	SpellParam<Vector3> origin;
	SpellParam<Vector3> ray;
	SpellParam<Number> max;

	public PieceOperatorEntityRaycast(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(origin = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false));
		addParam(ray = new ParamVector(SpellParam.GENERIC_NAME_RAY, SpellParam.GREEN, false));
		addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 originVal = this.getParamValue(context, origin);
		Vector3 rayVal = this.getParamValue(context, ray);

		if(originVal == null || rayVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		double maxLen = SpellHelpers.rangeLimitParam(this, context, max, SpellContext.MAX_DISTANCE);

		Entity entity = rayTraceEntities(context.focalPoint.level, originVal.toVec3D(), rayVal.toVec3D(),
				pred -> !pred.isSpectator() && pred.isAlive() && pred.isPickable() && !(pred instanceof ISpellImmune), maxLen);
		if(entity == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return entity;
	}

	/**
	 * [VanillaCopy]
	 * {@link net.minecraft.world.entity.projectile.ProjectileUtil#getEntityHitResult(Entity, Vec3, Vec3, AABB, Predicate, double)}
	 * (World, Entity, Vec3, Vec3, AxisAlignedBB, Predicate, double)}
	 * Some slight tweaks as we don't need an AABB provided to us, we can just make one.
	 */
	public static Entity rayTraceEntities(Level world, Vec3 positionVector, Vec3 lookVector, Predicate<Entity> predicate, double maxDistance) {
		double distance = maxDistance;
		Entity entity = null;

		Vec3 reachVector = positionVector.add(lookVector.scale(maxDistance));
		AABB aabb = new AABB(positionVector.x, positionVector.y, positionVector.z, reachVector.x, reachVector.y, reachVector.z).inflate(1f, 1f, 1f);
		for(Entity entity1 : world.getEntities((Entity) null, aabb, predicate)) {
			float collisionBorderSize = entity1.getPickRadius();
			AABB axisalignedbb = entity1.getBoundingBox().inflate(collisionBorderSize);
			Optional<Vec3> optional = axisalignedbb.clip(positionVector, reachVector);
			if(axisalignedbb.contains(positionVector)) {
				if(0.0D < distance || distance == 0.0D) {
					entity = entity1;
					distance = 0.0D;
				}
			} else if(optional.isPresent()) {
				double distanceTo = positionVector.distanceTo(optional.get());
				if(distanceTo < distance) {
					entity = entity1;
					distance = distanceTo;
				}
			}
		}
		return entity;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
