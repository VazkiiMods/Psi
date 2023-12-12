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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorBlockComparatorStrength extends PieceOperator {

	SpellParam<Vector3> axisParam;
	SpellParam<Vector3> target;

	public PieceOperatorBlockComparatorStrength(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
		addParam(axisParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, target, false, false);

		Direction whichWay = SpellHelpers.getFacing(this, context, axisParam);
		if(whichWay == Direction.UP || whichWay == Direction.DOWN) {
			throw new SpellRuntimeException(SpellRuntimeException.COMPARATOR);
		}

		BlockState state = Blocks.COMPARATOR.defaultBlockState()
				.setValue(HorizontalDirectionalBlock.FACING, whichWay.getOpposite());

		return ((ComparatorBlock) Blocks.COMPARATOR).getInputSignal(context.focalPoint.level(), pos.relative(whichWay), state) * 1.0;
	}

	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
