/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickExplode extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Number> power;

	public PieceTrickExplode(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_POWER, true).max(0.5).mul(70).floor());
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_POWER, true).max(0.5).mul(210).floor());
	}

	private static boolean isLiquid(BlockState pState) {
		return pState == Blocks.WATER.defaultBlockState() || pState == Blocks.LAVA.defaultBlockState();
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(power = new ParamNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double powerVal = this.<Double>getParamEvaluation(power);
		if(powerVal == null || powerVal <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		}

		powerVal = Math.max(0.5, powerVal);

		meta.addStat(EnumSpellStat.POTENCY, (int) (powerVal * 70));
		meta.addStat(EnumSpellStat.COST, (int) (powerVal * 210));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		double powerVal = this.getParamValue(context, power).doubleValue();

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();
		BlockState state = context.focalPoint.getCommandSenderWorld().getBlockState(pos);

		context.focalPoint.getCommandSenderWorld().explode(context.focalPoint, positionVal.x, positionVal.y, positionVal.z, (float) powerVal, isLiquid(state) ? Level.ExplosionInteraction.NONE : Level.ExplosionInteraction.TNT);
		return null;
	}

}
