/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

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

import java.util.LinkedHashSet;
import java.util.Map;

public class PieceTrickMoveBlockSequence extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> target;
	SpellParam<Vector3> direction;
	SpellParam<Number> maxBlocks;

	public PieceTrickMoveBlockSequence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
		addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_DIRECTION, SpellParam.GREEN, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 10));
		meta.addStat(EnumSpellStat.COST, (int) ((18 + (maxBlocksVal - 1) * 10.5)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 directionVal = SpellHelpers.getVector3(this, context, direction, false, true);
		Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
		Vector3 targetVal = SpellHelpers.getVector3(this, context, target, false, false);
		int maxBlocksVal = this.getParamValue(context, maxBlocks).intValue();

		Map<BlockPos, BlockState> toSet = Maps.newHashMap();
		Map<BlockPos, BlockState> toRemove = Maps.newHashMap();

		Vector3 directNorm = directionVal.copy().normalize();
		Vector3 targetNorm = targetVal.copy().normalize();
		LinkedHashSet<BlockPos> positions = MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksVal)).toVec3D(), maxBlocksVal);
		for (BlockPos blockPos : positions) {
			World world = context.caster.world;
			BlockState state = world.getBlockState(blockPos);

			if (world.getTileEntity(blockPos) != null ||
					state.getPushReaction() != PushReaction.NORMAL ||
					state.getPlayerRelativeBlockHardness(context.caster, world, blockPos) <= 0 ||
					!PieceTrickBreakBlock.canHarvestBlock(state, context.caster, world, blockPos, context.tool) ||
					!SpellHelpers.isBlockPosInRadius(context, blockPos) ||
					!world.isBlockModifiable(context.caster, blockPos) ||
					world.isAirBlock(blockPos)) {
				continue;
			}

			BlockPos pushToPos = blockPos.add(directNorm.x, directNorm.y, directNorm.z);
			BlockPos nextPos = blockPos.add(targetNorm.x, targetNorm.y, targetNorm.z);
			BlockState pushToState = world.getBlockState(pushToPos);
			BlockPos nextPosPushPos = nextPos.add(directNorm.x, directNorm.y, directNorm.z);
			if (!world.isBlockModifiable(context.caster, pushToPos)) {
				continue;
			}

			BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, state, context.caster);

			if (MinecraftForge.EVENT_BUS.post(event)) {
				continue;
			}

			if ((nextPos.equals(pushToPos) &&
					((positions.contains(nextPos) && (world.isAirBlock(nextPosPushPos) || world.getBlockState(nextPosPushPos).getMaterial().isReplaceable() || positions.contains(nextPosPushPos))))) ||
					(world.isAirBlock(pushToPos) || pushToState.getMaterial().isReplaceable()) &&
							!(pushToPos.getY() < 0 || pushToPos.getY() > 256)) {
				toRemove.put(blockPos, state);
				toSet.put(pushToPos, state);
			} else {
				return null;
			}
		}

		for (Map.Entry<BlockPos, BlockState> pairtoRemove : toRemove.entrySet()) {
			context.caster.world.removeBlock(pairtoRemove.getKey(), true);
			context.caster.world.playEvent(2001, pairtoRemove.getKey(), Block.getStateId(pairtoRemove.getValue()));
		}

		for (Map.Entry<BlockPos, BlockState> pairToSet : toSet.entrySet()) {
			context.caster.world.setBlockState(pairToSet.getKey(), pairToSet.getValue());
		}

		return null;
	}

}
