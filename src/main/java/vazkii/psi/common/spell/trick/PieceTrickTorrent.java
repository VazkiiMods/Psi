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
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.level.BlockEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import javax.annotation.Nullable;

public class PieceTrickTorrent extends PieceTrick {

    SpellParam<Vector3> position;

    public PieceTrickTorrent(Spell spell) {
        super(spell);
        setStatLabel(EnumSpellStat.POTENCY, new StatLabel(20));
        setStatLabel(EnumSpellStat.COST, new StatLabel(80));
    }

    // [VanillaCopy] BucketItem.tryPlaceContainingLiquid because buckets are dumb
    public static boolean placeWater(@Nullable Player playerIn, Level worldIn, BlockPos pos) {
        if (playerIn == null || !worldIn.hasChunkAt(pos) || !worldIn.mayInteract(playerIn, pos)) {
            return false;
        }
        BlockState blockstate = worldIn.getBlockState(pos);
        boolean flag = blockstate.canBeReplaced(Fluids.WATER);
        if (blockstate.isAir() || flag || blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(playerIn, worldIn, pos, blockstate, Fluids.WATER)) {
            if (worldIn.dimensionType().ultraWarm()) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(playerIn, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else if (blockstate.getBlock() instanceof LiquidBlockContainer) {
                if (((LiquidBlockContainer) blockstate.getBlock()).placeLiquid(worldIn, pos, blockstate, Fluids.WATER.getSource(false))) {
                    worldIn.playSound(playerIn, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                if (!worldIn.isClientSide && flag && !isLiquid(blockstate)) {
                    worldIn.destroyBlock(pos, true);
                }

                worldIn.playSound(playerIn, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlock(pos, Fluids.WATER.defaultFluidState().createLegacyBlock(), 11);
            }

            return true;
        }
        return false;
    }

    private static boolean isLiquid(BlockState pState) {
        return pState == Blocks.WATER.defaultBlockState() || pState == Blocks.LAVA.defaultBlockState();
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 20);
        meta.addStat(EnumSpellStat.COST, 80);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.focalPoint.getCommandSenderWorld().dimensionType().ultraWarm()) {
            return null;
        }
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getCommandSenderWorld().dimension(), context.focalPoint.getCommandSenderWorld(), pos), context.focalPoint.getCommandSenderWorld().getBlockState(pos.relative(Direction.UP)), context.caster);
        NeoForge.EVENT_BUS.post(placeEvent);
        if (placeEvent.isCanceled()) {
            return null;
        }
        return placeWater(context.caster, context.focalPoint.level(), pos);
    }
}
