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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.MathHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbySmeltables;

public class PieceTrickSmeltBlockSequence extends PieceTrick {
	SpellParam<Vector3> position;
	SpellParam<Vector3> target;
	SpellParam<Number> maxBlocks;

	public PieceTrickSmeltBlockSequence(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).mul(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).sub(1).parenthesize().mul(64).add(96));
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
		addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		
		double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 20));
		meta.addStat(EnumSpellStat.COST, (int) ((96 + (maxBlocksVal - 1) * 64)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);
		int maxBlocksInt = this.getParamValue(context, maxBlocks).intValue();

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		ItemStack tool = context.tool;
		if(tool.isEmpty()) {
			tool = PsiAPI.getPlayerCAD(context.caster);
		}

		Vector3 targetNorm = targetVal.copy().normalize();
		for(BlockPos blockPos : MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksInt)).toVec3D(), maxBlocksInt)) {
			if(!context.isInRadius(Vector3.fromBlockPos(blockPos))) {
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
			}

			if(!context.focalPoint.getCommandSenderWorld().mayInteract(context.caster, blockPos)) {
				return null;
			}

			BlockState state = context.focalPoint.getCommandSenderWorld().getBlockState(blockPos);
			Block block = state.getBlock();
			ItemStack stack = new ItemStack(block);
			BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, context.focalPoint.level, blockPos, tool);
			MinecraftForge.EVENT_BUS.post(event);
			if(event.isCanceled()) {
				return null;
			}
			ItemStack result = PieceSelectorNearbySmeltables.simulateSmelt(context.focalPoint.getCommandSenderWorld(), stack);
			if(!result.isEmpty()) {
				Item item = result.getItem();
				Block block1 = Block.byItem(item);
				if(block1 != Blocks.AIR) {
					context.focalPoint.getCommandSenderWorld().setBlockAndUpdate(blockPos, block1.defaultBlockState());
					context.focalPoint.getCommandSenderWorld().levelEvent(2001, blockPos, Block.getId(block1.defaultBlockState()));
				}
			}

		}

		return null;
	}
}
