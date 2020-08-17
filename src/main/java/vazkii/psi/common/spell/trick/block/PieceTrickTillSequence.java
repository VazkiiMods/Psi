/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.util.math.BlockPos;

import vazkii.psi.api.internal.MathHelper;
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
import vazkii.psi.common.core.helpers.SpellHelpers;

public class PieceTrickTillSequence extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> target;
	SpellParam<Number> maxBlocks;

	public PieceTrickTillSequence(Spell spell) {
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

		double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 10));
		meta.addStat(EnumSpellStat.COST, (int) ((12 + (maxBlocksVal - 1) * 7)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
		Vector3 targetVal = SpellHelpers.getVector3(this, context, target, false, false);
		int maxBlocksInt = this.getParamValue(context, maxBlocks).intValue();

		Vector3 targetNorm = targetVal.copy().normalize();

		for (BlockPos blockPos : MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksInt)).toVec3D(), maxBlocksInt)) {

			if (SpellHelpers.isBlockPosInRadius(context, blockPos)) {
				PieceTrickTill.tillBlock(context.caster, context.caster.world, blockPos);
			}

		}
		return null;

	}
}
