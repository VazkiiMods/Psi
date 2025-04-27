/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.vector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRaycast extends PieceOperator {

	SpellParam<Vector3> origin;
	SpellParam<Vector3> ray;
	SpellParam<Number> max;

	public PieceOperatorVectorRaycast(Spell spell) {
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

		BlockHitResult pos = raycast(context.caster, originVal, rayVal, maxLen);
		if(pos.getType() == HitResult.Type.MISS) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		return Vector3.fromBlockPos(pos.getBlockPos());
	}

	public static BlockHitResult raycast(Entity e, double len) {
		Vector3 vec = Vector3.fromEntity(e);
		vec.add(0, e.getEyeHeight(), 0);

		Vec3 look = e.getLookAngle();

		return raycast(e, vec, new Vector3(look), len);
	}

	public static BlockHitResult raycast(Entity entity, Vector3 origin, Vector3 ray, double len) {
		Vector3 end = origin.copy().add(ray.copy().normalize().multiply(len));
		return entity.level.clip(new ClipContext(origin.toVec3D(), end.toVec3D(), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
