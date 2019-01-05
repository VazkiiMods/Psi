/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [24/01/2016, 16:20:58 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickBreakInSequence extends PieceTrick {

	SpellParam position;
	SpellParam target;
	SpellParam maxBlocks;

	public PieceTrickBreakInSequence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
		addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
		if(maxBlocksVal == null || maxBlocksVal <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 20));
		meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 25));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.getEntityWorld().isRemote)
			return null;

		Vector3 positionVal = this.<Vector3>getParamValue(context, position);
		Vector3 targetVal = this.<Vector3>getParamValue(context, target);
		Double maxBlocksVal = this.<Double>getParamValue(context, maxBlocks);
		int maxBlocksInt = maxBlocksVal.intValue();

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		int len = (int) targetVal.mag();
		Vector3 targetNorm = targetVal.copy().normalize();
		for(int i = 0; i < Math.min(len, maxBlocksInt); i++) {
			Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

			if(!context.isInRadius(blockVec))
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

			BlockPos pos = blockVec.toBlockPos();
			PieceTrickBreakBlock.removeBlockWithDrops(context, context.caster, context.caster.getEntityWorld(), context.tool, pos, true);
		}

		return null;
	}

}
