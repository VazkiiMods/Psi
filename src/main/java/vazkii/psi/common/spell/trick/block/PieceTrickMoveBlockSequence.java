/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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
		World world = context.caster.world;

		Map<BlockPos, BlockState> toSet = Maps.newHashMap();
		Map<BlockPos, BlockState> toRemove = Maps.newHashMap();

		Vector3 directNorm = directionVal.copy().normalize();
		Vector3 targetNorm = targetVal.copy().normalize();

		LinkedHashSet<BlockPos> positions = MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksVal)).toVec3D(), maxBlocksVal);
		LinkedHashSet<BlockPos> moveableBlocks = new LinkedHashSet<>();
		LinkedHashSet<BlockPos> immovableBlocks = new LinkedHashSet<>();

		for (BlockPos blockPos : positions) {
			BlockState state = world.getBlockState(blockPos);

			if (world.isAirBlock(blockPos)) {
				continue;
			}

			if (world.getTileEntity(blockPos) != null ||
					state.getPushReaction() != PushReaction.NORMAL ||
					state.getBlockHardness(world, blockPos) == -1 ||
					!PieceTrickBreakBlock.canHarvestBlock(state, context.caster, world, blockPos, context.getHarvestTool()) ||
					!SpellHelpers.isBlockPosInRadius(context, blockPos) ||
					!world.isBlockModifiable(context.caster, blockPos)) {
				immovableBlocks.add(blockPos);
				continue;
			}

			BlockPos pushToPos = blockPos.add(directNorm.x, directNorm.y, directNorm.z);
			boolean isOffWorld = pushToPos.getY() < 0 || pushToPos.getY() > 256;
			if (isOffWorld) {
				immovableBlocks.add(blockPos);
				continue;
			}

			BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, state, context.caster);

			if (MinecraftForge.EVENT_BUS.post(event)) {
				immovableBlocks.add(blockPos);
				continue;
			}
			moveableBlocks.add(blockPos);
		}

		outer:
		for (BlockPos blockPos : moveableBlocks) {
			BlockState state = world.getBlockState(blockPos);
			BlockPos pushToPos = blockPos.add(directNorm.x, directNorm.y, directNorm.z);
			BlockState pushToState = world.getBlockState(pushToPos);
			if (immovableBlocks.contains(pushToPos) || immovableBlocks.contains(blockPos)) {
				continue;
			}
			if (moveableBlocks.contains(pushToPos)) {
				BlockPos nextPos = pushToPos;
				while (moveableBlocks.contains(nextPos)) {
					BlockPos nextPosPushPos = nextPos.add(directNorm.x, directNorm.y, directNorm.z);
					BlockState nextPosPushPosState = world.getBlockState(nextPosPushPos);

					if (moveableBlocks.contains(nextPosPushPos)) {
						nextPos = nextPosPushPos;
						continue;
					}

					if (immovableBlocks.contains(nextPosPushPos) || !(world.isAirBlock(nextPosPushPos) || nextPosPushPosState.getMaterial().isReplaceable())) {
						continue outer;
					}
					break;
				}
			} else if (!(world.isAirBlock(pushToPos) || pushToState.getMaterial().isReplaceable())) {
				continue;
			}
			toRemove.put(blockPos, state);
			toSet.put(pushToPos, state);
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
