/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
import vazkii.psi.api.spell.StatLabel;
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

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();

		pos = pos.down();
		BlockState state = context.focalPoint.getEntityWorld().getBlockState(pos);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getEntityWorld().getDimensionKey(), context.focalPoint.getEntityWorld(), pos), context.focalPoint.getEntityWorld().getBlockState(pos.offset(Direction.UP)), context.caster);
		MinecraftForge.EVENT_BUS.post(placeEvent);
		if (placeEvent.isCanceled()) {
			return null;
		}
		if (state.isAir(context.focalPoint.getEntityWorld(), pos) || state.getMaterial().isReplaceable()) {
			context.focalPoint.getEntityWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
		} else {
			pos = pos.up();
			state = context.focalPoint.getEntityWorld().getBlockState(pos);
			if (state.isAir(context.focalPoint.getEntityWorld(), pos) || state.getMaterial().isReplaceable()) {
				context.focalPoint.getEntityWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
			}
		}

		return null;
	}

}
