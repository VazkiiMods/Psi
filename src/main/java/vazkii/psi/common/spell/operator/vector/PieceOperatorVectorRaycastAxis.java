/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.vector;

import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVectorRaycastAxis extends PieceOperator {

	SpellParam<Vector3> origin;
	SpellParam<Vector3> ray;
	SpellParam<Number> max;

	public PieceOperatorVectorRaycastAxis(Spell spell) {
		super(spell);
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

		Vector3 end = originVal.copy().add(rayVal.copy().normalize().multiply(maxLen));

		BlockHitResult pos = context.caster.getCommandSenderWorld().clip(new ClipContext(originVal.toVec3D(), end.toVec3D(), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, context.caster));
		if(pos.getType() == HitResult.Type.MISS) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		Direction facing = pos.getDirection();
		return new Vector3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
