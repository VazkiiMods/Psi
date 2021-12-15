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
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellHelpers;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import javax.annotation.Nullable;

public class PieceTrickTorrent extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickTorrent(Spell spell) {
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
		meta.addStat(EnumSpellStat.COST, 80);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if (context.focalPoint.getEntityWorld().getDimensionType().isUltrawarm()) {
			return null;
		}
		BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
		BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(BlockSnapshot.create(context.focalPoint.getEntityWorld().getDimensionKey(), context.focalPoint.getEntityWorld(), pos), context.focalPoint.getEntityWorld().getBlockState(pos.offset(Direction.UP)), context.caster);
		MinecraftForge.EVENT_BUS.post(placeEvent);
		if (placeEvent.isCanceled()) {
			return null;
		}
		return placeWater(context.caster, context.caster.world, pos);
	}

	// [VanillaCopy] BucketItem.tryPlaceContainingLiquid because buckets are dumb
	public static boolean placeWater(@Nullable PlayerEntity playerIn, World worldIn, BlockPos pos) {
		if (!worldIn.isBlockLoaded(pos) || !worldIn.isBlockModifiable(playerIn, pos)) {
			return false;
		}
		BlockState blockstate = worldIn.getBlockState(pos);
		Material material = blockstate.getMaterial();
		boolean flag = blockstate.isReplaceable(Fluids.WATER);
		if (blockstate.isAir() || flag || blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, pos, blockstate, Fluids.WATER)) {
			if (worldIn.getDimensionType().isUltrawarm()) {
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

				for (int l = 0; l < 8; ++l) {
					worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
				}
			} else if (blockstate.getBlock() instanceof ILiquidContainer) {
				if (((ILiquidContainer) blockstate.getBlock()).receiveFluid(worldIn, pos, blockstate, Fluids.WATER.getStillFluidState(false))) {
					worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			} else {
				if (!worldIn.isRemote && flag && !material.isLiquid()) {
					worldIn.destroyBlock(pos, true);
				}

				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
				worldIn.setBlockState(pos, Fluids.WATER.getDefaultState().getBlockState(), 11);
			}

			return true;
		}
		return false;
	}

}
