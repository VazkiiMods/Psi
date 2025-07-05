/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;

public class PieceTrickConjureBlockSequence extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> target;
	SpellParam<Number> maxBlocks;
	SpellParam<Number> time;

	public PieceTrickConjureBlockSequence(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).add(15));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).sub(1).parenthesize().mul(14).sub(24));
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
		addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
		if(maxBlocksVal == null || maxBlocksVal <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 15));
		meta.addStat(EnumSpellStat.COST, (int) ((24 + (maxBlocksVal - 1) * 14)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);
		int maxBlocksInt = this.getParamValue(context, maxBlocks).intValue();
		Number timeVal = this.getParamValue(context, time);

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		Vector3 targetNorm = targetVal.copy().normalize();
		Level world = context.focalPoint.getCommandSenderWorld();

		for(BlockPos blockPos : MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksInt)).toVec3D(), maxBlocksInt)) {
			if(!context.isInRadius(Vector3.fromBlockPos(blockPos))) {
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
			}

			if(!world.mayInteract(context.caster, blockPos)) {
				continue;
			}

			PieceTrickConjureBlock.conjure(context, timeVal, blockPos, world, messWithState(ModBlocks.conjured.defaultBlockState()));
		}

		return null;
	}

	public BlockState messWithState(BlockState state) {
		return state.setValue(BlockConjured.SOLID, true);
	}

}
