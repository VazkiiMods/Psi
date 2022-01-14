/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbySmeltables;

public class PieceTrickSmeltBlock extends PieceTrick {

	SpellParam<Vector3> position;

	public PieceTrickSmeltBlock(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(20));
		setStatLabel(EnumSpellStat.COST, new StatLabel(80));
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
	@SuppressWarnings("deprecation")
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);

		ItemStack tool = context.tool;
		if (tool.isEmpty()) {
			tool = PsiAPI.getPlayerCAD(context.caster);
		}

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if (!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();
		if (!context.focalPoint.getEntityWorld().isBlockModifiable(context.caster, pos)) {
			return null;
		}

		BlockState state = context.focalPoint.getEntityWorld().getBlockState(pos);
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(block);
		BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, context.caster.world, pos, tool);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return null;
		}
		ItemStack result = PieceSelectorNearbySmeltables.simulateSmelt(context.focalPoint.getEntityWorld(), stack);
		if (!result.isEmpty()) {
			Item item = result.getItem();
			Block block1 = Block.getBlockFromItem(item);
			if (block1 != Blocks.AIR) {
				context.focalPoint.getEntityWorld().setBlockState(pos, block1.getDefaultState());
				context.focalPoint.getEntityWorld().playEvent(2001, pos, Block.getStateId(block1.getDefaultState()));
			}
		}

		return null;
	}

}
