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
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

public class PieceTrickCollapseBlockSequence extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> target;
	SpellParam<Number> maxBlocks;

	public PieceTrickCollapseBlockSequence(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).mul(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(SpellParam.GENERIC_NAME_MAX, true).sub(1).parenthesize().mul(150).add(100));
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

		Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
		if (maxBlocksVal == null || maxBlocksVal <= 0) {
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
		}

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 20));
		meta.addStat(EnumSpellStat.COST, (int) ((150 + (maxBlocksVal - 1) * 100)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);
		int maxBlocksInt = this.getParamValue(context, maxBlocks).intValue();

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		ItemStack tool = context.tool;
		if (tool.isEmpty()) {
			tool = PsiAPI.getPlayerCAD(context.caster);
		}

		Level world = context.caster.level;
		Vector3 targetNorm = targetVal.copy().normalize();
		for (BlockPos blockPos : MathHelper.getBlocksAlongRay(positionVal.toVec3D(), positionVal.copy().add(targetNorm.copy().multiply(maxBlocksInt)).toVec3D(), maxBlocksInt)) {
			if (!context.isInRadius(Vector3.fromBlockPos(blockPos))) {
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
			}
			BlockPos posDown = blockPos.below();
			BlockState state = world.getBlockState(blockPos);
			BlockState stateDown = world.getBlockState(posDown);

			if (!world.mayInteract(context.caster, blockPos)) {
				return null;
			}

			if (stateDown.isAir() && state.getDestroySpeed(world, blockPos) != -1 &&
					PieceTrickBreakBlock.canHarvestBlock(state, context.caster, world, blockPos, tool) &&
					world.getBlockEntity(blockPos) == null) {

				BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, world, blockPos, tool);
				MinecraftForge.EVENT_BUS.post(event);
				if (event.isCanceled()) {
					return null;
				}

				FallingBlockEntity.fall(world, blockPos, state);
			}
		}

		return null;
	}
}
