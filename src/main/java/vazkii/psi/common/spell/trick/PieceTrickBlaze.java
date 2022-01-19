/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickBlaze extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickBlaze(Spell spell) {
		super(spell);
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

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();

		pos = pos.below();
		BlockState state = context.focalPoint.getCommandSenderWorld().getBlockState(pos);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getCommandSenderWorld().dimension(), context.focalPoint.getCommandSenderWorld(), pos), context.focalPoint.getCommandSenderWorld().getBlockState(pos.relative(Direction.UP)), context.caster);
		MinecraftForge.EVENT_BUS.post(placeEvent);
		if (placeEvent.isCanceled()) {
			return null;
		}
		if (state.isAir(context.focalPoint.getCommandSenderWorld(), pos) || state.getMaterial().isReplaceable()) {
			context.focalPoint.getCommandSenderWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
		} else {
			pos = pos.above();
			state = context.focalPoint.getCommandSenderWorld().getBlockState(pos);
			if (state.isAir(context.focalPoint.getCommandSenderWorld(), pos) || state.getMaterial().isReplaceable()) {
				context.focalPoint.getCommandSenderWorld().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
			}
		}

		return null;
	}

}
