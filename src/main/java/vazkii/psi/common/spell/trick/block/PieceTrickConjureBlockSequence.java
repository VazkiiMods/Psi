/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 17:51:29 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		if(maxBlocksVal == null || maxBlocksVal <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 15));
		meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 20));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);
		int maxBlocksInt = this.getParamValue(context, maxBlocks).intValue();
		double timeVal = this.getParamValue(context, time).doubleValue();

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		int len = (int) targetVal.mag();
		Vector3 targetNorm = targetVal.copy().normalize();
		World world = context.caster.getEntityWorld();

		for(int i = 0; i < Math.min(len, maxBlocksInt); i++) {
			Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

			if(!context.isInRadius(blockVec))
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

			BlockPos pos = blockVec.toBlockPos();
			if(!world.isBlockModifiable(context.caster, pos))
				continue;

			PieceTrickConjureBlock.conjure(context, timeVal, pos, world, messWithState(ModBlocks.conjured.getDefaultState()));
		}

		return null;
	}

	public BlockState messWithState(BlockState state) {
		return state.with(BlockConjured.SOLID, true);
	}

}
