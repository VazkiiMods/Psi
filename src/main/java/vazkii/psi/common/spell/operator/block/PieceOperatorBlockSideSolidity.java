/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.interval.IntervalNumber;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorBlockSideSolidity extends PieceOperator {
	SpellParam<Vector3> axisParam;
	SpellParam<Vector3> target;

	public PieceOperatorBlockSideSolidity(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
		addParam(axisParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
	}
	
	@Override
	public @NotNull IntervalNumber evaluate() {
		return IntervalNumber.zeroToOne;
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, target, false, false);
		Direction facing = SpellHelpers.getFacing(this, context, axisParam);

		BlockState state = context.focalPoint.level.getBlockState(pos);
		return state.isFaceSturdy(context.focalPoint.level, pos, facing) ? 1.0D : 0.D;
	}

	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
