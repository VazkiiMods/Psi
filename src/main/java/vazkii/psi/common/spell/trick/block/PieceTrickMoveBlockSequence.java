/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class PieceTrickMoveBlockSequence extends PieceTrick {

    SpellParam<Vector3> position;
    SpellParam<Vector3> target;
    SpellParam<Vector3> direction;
    SpellParam<Number> maxBlocks;

    public PieceTrickMoveBlockSequence(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).mul(10));
        setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).sub(1).parenthesize().mul(10.5).add(18).floor());
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
        Level world = context.focalPoint.level();

        Map<BlockPos, BlockState> toSet = new HashMap<>();
        Map<BlockPos, BlockState> toRemove = new HashMap<>();

        Vector3 directNorm = directionVal.copy().normalize();
        Vector3 targetNorm = targetVal.copy().normalize();

        LinkedHashSet<BlockPos> positions = MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksVal)).toVec3D(), maxBlocksVal);
        LinkedHashSet<BlockPos> moveableBlocks = new LinkedHashSet<>();
        LinkedHashSet<BlockPos> immovableBlocks = new LinkedHashSet<>();

        /*
         * TODO: Find a better solution than this bandaid for block duping (see #740)
         * A possible solution is moving this logic to {@link PieceTrickBreakBlock}
         * As well as passing the spell context to it as a parameter. The Spell Context would need to have a way to
         * check if it has been delayed or not
         * Since there are legitimate use cases besides duping when you want to move a block that is in the same
         * position that you previously had broken.
         */
        if (context.positionBroken != null) {
            immovableBlocks.add(context.positionBroken.getBlockPos());
        }

        for (BlockPos blockPos : positions) {
            BlockState state = world.getBlockState(blockPos);

            if (world.isEmptyBlock(blockPos)) {
                continue;
            }

            if (world.getBlockEntity(blockPos) != null ||
                    state.getPistonPushReaction() != PushReaction.NORMAL ||
                    state.getDestroySpeed(world, blockPos) == -1 ||
                    !PieceTrickBreakBlock.canHarvestBlock(state, context.caster, world, blockPos, context.getHarvestTool()) ||
                    !SpellHelpers.isBlockPosInRadius(context, blockPos) ||
                    !world.mayInteract(context.caster, blockPos)) {
                immovableBlocks.add(blockPos);
                continue;
            }

            BlockPos pushToPos = blockPos.offset((int) directNorm.x, (int) directNorm.y, (int) directNorm.z);
            boolean isOffWorld = pushToPos.getY() < 0 || pushToPos.getY() > 256;
            if (isOffWorld) {
                immovableBlocks.add(blockPos);
                continue;
            }

            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, state, context.caster);

            if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
                immovableBlocks.add(blockPos);
                continue;
            }
            moveableBlocks.add(blockPos);
        }

        outer:
        for (BlockPos blockPos : moveableBlocks) {
            BlockState state = world.getBlockState(blockPos);
            BlockPos pushToPos = blockPos.offset((int) directNorm.x, (int) directNorm.y, (int) directNorm.z);
            BlockState pushToState = world.getBlockState(pushToPos);
            if (immovableBlocks.contains(pushToPos) || immovableBlocks.contains(blockPos)) {
                continue;
            }
            if (moveableBlocks.contains(pushToPos)) {
                BlockPos nextPos = pushToPos;
                while (moveableBlocks.contains(nextPos)) {
                    BlockPos nextPosPushPos = nextPos.offset((int) directNorm.x, (int) directNorm.y, (int) directNorm.z);
                    BlockState nextPosPushPosState = world.getBlockState(nextPosPushPos);

                    if (moveableBlocks.contains(nextPosPushPos)) {
                        nextPos = nextPosPushPos;
                        continue;
                    }

                    if (immovableBlocks.contains(nextPosPushPos) || !(world.isEmptyBlock(nextPosPushPos) || nextPosPushPosState.canBeReplaced())) {
                        continue outer;
                    }
                    break;
                }
            } else if (!(world.isEmptyBlock(pushToPos) || pushToState.canBeReplaced())) {
                continue;
            }
            toRemove.put(blockPos, state);
            toSet.put(pushToPos, state);
        }

        for (Map.Entry<BlockPos, BlockState> pairtoRemove : toRemove.entrySet()) {
            context.focalPoint.level().removeBlock(pairtoRemove.getKey(), true);
            context.focalPoint.level().levelEvent(2001, pairtoRemove.getKey(), Block.getId(pairtoRemove.getValue()));
        }

        for (Map.Entry<BlockPos, BlockState> pairToSet : toSet.entrySet()) {
            context.focalPoint.level().setBlockAndUpdate(pairToSet.getKey(), pairToSet.getValue());
        }

        return null;
    }

}
