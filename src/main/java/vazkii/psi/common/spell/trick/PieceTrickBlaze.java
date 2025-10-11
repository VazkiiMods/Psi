/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.level.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickBlaze extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickBlaze(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(40));
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 40);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();

		pos = pos.below();
		BlockState state = context.focalPoint.getCommandSenderWorld().getBlockState(pos);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getCommandSenderWorld().dimension(), context.focalPoint.getCommandSenderWorld(), pos), context.focalPoint.getCommandSenderWorld().getBlockState(pos.relative(Direction.UP)), context.caster);
		NeoForge.EVENT_BUS.post(placeEvent);
		if(placeEvent.isCanceled()) {
			return null;
		}
		if(state.isAir() || state.canBeReplaced()) {
			context.focalPoint.getCommandSenderWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
		} else {
			pos = pos.above();
			state = context.focalPoint.getCommandSenderWorld().getBlockState(pos);
			if(state.isAir() || state.canBeReplaced()) {
				context.focalPoint.getCommandSenderWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
			}
		}

		return null;
	}

}
