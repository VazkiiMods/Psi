/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.level.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSmite extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickSmite(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(100));
		setStatLabel(EnumSpellStat.COST, new StatLabel(400));
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, 100);
		meta.addStat(EnumSpellStat.COST, 400);
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

		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getCommandSenderWorld().dimension(), context.focalPoint.getCommandSenderWorld(), positionVal.toBlockPos()), context.focalPoint.getCommandSenderWorld().getBlockState(positionVal.toBlockPos().relative(Direction.UP)), context.caster);
		NeoForge.EVENT_BUS.post(placeEvent);
		if(placeEvent.isCanceled()) {
			return null;
		}

		if(context.focalPoint.getCommandSenderWorld() instanceof ServerLevel) {
			LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, context.focalPoint.level());
			lightning.setPosRaw(positionVal.x, positionVal.y, positionVal.z);
			context.focalPoint.getCommandSenderWorld().addFreshEntity(lightning);
		}

		return null;
	}

}
