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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
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
		BlockState state = context.caster.level.getBlockState(pos);
		ItemStack tool = context.tool;
		if (tool.isEmpty()) {
			tool = PsiAPI.getPlayerCAD(context.caster);
		}
		//TODO Perhaps a more granular mining level solution?
		if (PieceTrickBreakBlock.canHarvestBlock(state, context.caster, context.focalPoint.level, pos, tool)) {
			return 1.0D;
		} else {
			return 0.0D;
		}
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
