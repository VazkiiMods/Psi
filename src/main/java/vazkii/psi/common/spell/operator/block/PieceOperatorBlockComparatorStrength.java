/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.helpers.SpellHelpers;

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
		if (whichWay == Direction.UP || whichWay == Direction.DOWN) {
			throw new SpellRuntimeException(SpellRuntimeException.COMPARATOR);
		}

		BlockState state = Blocks.COMPARATOR.getDefaultState()
				.with(HorizontalBlock.HORIZONTAL_FACING, whichWay.getOpposite());

		return ((ComparatorBlock) Blocks.COMPARATOR).calculateInputStrength(context.caster.world, pos.offset(whichWay), state) * 1.0;
	}

	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
