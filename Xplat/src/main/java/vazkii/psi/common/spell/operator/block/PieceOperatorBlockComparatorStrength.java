/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.util.List;

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

		return getInputSignal(context.focalPoint.level(), pos.relative(whichWay), state) * 1.0;
	}

	private static int getInputSignal(Level level, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
		BlockPos inputPos = pos.relative(direction);
		BlockState inputState = level.getBlockState(inputPos);
		int signal = level.getSignal(inputPos, direction);
		if(signal < 15 && inputState.is(Blocks.REDSTONE_WIRE)) {
			signal = Math.max(signal, inputState.getValue(RedStoneWireBlock.POWER));
		}

		if(inputState.hasAnalogOutputSignal()) {
			return inputState.getAnalogOutputSignal(level, inputPos);
		}
		if(signal >= 15 || !inputState.isRedstoneConductor(level, inputPos)) {
			return signal;
		}

		inputPos = inputPos.relative(direction);
		inputState = level.getBlockState(inputPos);
		ItemFrame frame = getItemFrame(level, direction, inputPos);
		int analogSignal = Math.max(
				frame == null ? Integer.MIN_VALUE : frame.getAnalogOutput(),
				inputState.hasAnalogOutputSignal()
						? inputState.getAnalogOutputSignal(level, inputPos)
						: Integer.MIN_VALUE);
		return analogSignal == Integer.MIN_VALUE ? signal : analogSignal;
	}

	private static ItemFrame getItemFrame(Level level, Direction facing, BlockPos pos) {
		List<ItemFrame> frames = level.getEntitiesOfClass(ItemFrame.class, new AABB(pos),
				frame -> frame != null && frame.getDirection() == facing);
		return frames.size() == 1 ? frames.getFirst() : null;
	}

	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
