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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

import java.util.List;
import java.util.Optional;

public class PieceOperatorFocusedEntity extends PieceOperator {

	SpellParam<Entity> target;

	public PieceOperatorFocusedEntity(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false));
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

	public static Entity getEntityLookedAt(Entity e) {
		Entity foundEntity = null;

		final double finalDistance = 32;
		double distance = finalDistance;
		HitResult pos = PieceOperatorVectorRaycast.raycast(e, finalDistance);
		Vec3 positionVector = e.position();
		if(e instanceof Player) {
			positionVector = positionVector.add(0, e.getEyeHeight(), 0);
		}

		if(pos != null) {
			distance = pos.getLocation().distanceTo(positionVector);
		}

		Vec3 lookVector = e.getLookAngle();
		Vec3 reachVector = positionVector.add(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance);

		Entity lookedEntity = null;
		List<Entity> entitiesInBoundingBox = e.getCommandSenderWorld().getEntities(e, e.getBoundingBox().inflate(lookVector.x * finalDistance, lookVector.y * finalDistance, lookVector.z * finalDistance).inflate(1F, 1F, 1F));
		double minDistance = distance;

		for(Entity entity : entitiesInBoundingBox) {
			if(entity.isPickable()) {
				float collisionBorderSize = entity.getPickRadius();
				AABB hitbox = entity.getBoundingBox().inflate(collisionBorderSize, collisionBorderSize, collisionBorderSize);
				Optional<Vec3> interceptPosition = hitbox.clip(positionVector, reachVector);

				if(hitbox.contains(positionVector)) {
					if(0.0D < minDistance || minDistance == 0.0D) {
						lookedEntity = entity;
						minDistance = 0.0D;
					}
				} else if(interceptPosition.isPresent()) {
					double distanceToEntity = positionVector.distanceTo(interceptPosition.get());

					if(distanceToEntity < minDistance || minDistance == 0.0D) {
						lookedEntity = entity;
						minDistance = distanceToEntity;
					}
				}
			}

			if(lookedEntity != null && (minDistance < distance || pos == null)) {
				foundEntity = lookedEntity;
			}
		}

		return foundEntity;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Entity.class;
	}

}
