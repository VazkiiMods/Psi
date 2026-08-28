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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.internal.SpatialHelper;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

public class PieceOperatorFocusedEntity extends PieceOperator {

	SpellParam<Entity> target;

	public PieceOperatorFocusedEntity(Spell spell) {
		super(spell);
	}

	public static Entity getEntityLookedAt(Entity e) {
		final double finalDistance = 32;
		HitResult pos = PieceOperatorVectorRaycast.raycast(e, finalDistance);
		Vec3 positionVector = e.position();
		if(e instanceof Player) {
			positionVector = positionVector.add(0, e.getEyeHeight(), 0);
		}

		Vec3 lookVector = e.getLookAngle();
		Vec3 reachVector = positionVector.add(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance);
		AABB area = e.getBoundingBox().expandTowards(lookVector.scale(finalDistance)).inflate(1);
		double blockDistance = SpatialHelper.distanceSquared(e.level(), positionVector, pos.getLocation());
		EntityHitResult hit = ProjectileUtil.getEntityHitResult(e, positionVector, reachVector, area, Entity::isPickable, blockDistance);
		return hit == null ? null : hit.getEntity();
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity e = this.getParamValue(context, target);

		if(e == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		Entity looked = getEntityLookedAt(e);
		if(looked == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}

		return looked;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
