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
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

public class PieceOperatorBlockMiningLevel extends PieceOperator {

	SpellParam<Vector3> position;

	public PieceOperatorBlockMiningLevel(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, position, false, false);
		BlockState state = context.focalPoint.level().getBlockState(pos);

		//TODO Fix low mining level items returning 1
		return PieceTrickBreakBlock.getHarvestLevel(state);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
