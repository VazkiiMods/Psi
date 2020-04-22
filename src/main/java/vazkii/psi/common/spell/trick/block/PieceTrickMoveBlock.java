/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [01/02/2016, 19:59:33 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickMoveBlock extends PieceTrick {

	SpellParam position;
	SpellParam target;

	public PieceTrickMoveBlock(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 10);
		meta.addStat(EnumSpellStat.COST, 15);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		ItemStack tool = context.tool;
		if (tool.isEmpty())
			tool = PsiAPI.getPlayerCAD(context.caster);

		World world = context.caster.getEntityWorld();
		BlockPos pos = positionVal.toBlockPos();
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(world.getTileEntity(pos) != null || state.getPushReaction() != EnumPushReaction.NORMAL ||
				!block.canSilkHarvest(world, pos, state, context.caster) ||
				state.getBlockHardness(world, pos) <= 0 ||
				!PieceTrickBreakBlock.canHarvestBlock(block, context.caster, world, pos, tool))
			return null;
		
		BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, world, pos, tool);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled())
			return null;
		
		if(!targetVal.isAxial() || targetVal.isZero())
			return null;

		Vector3 axis = targetVal.normalize();
		int x = pos.getX() + (int) axis.x;
		int y = pos.getY() + (int) axis.y;
		int z = pos.getZ() + (int) axis.z;
		BlockPos pos1 = new BlockPos(x, y, z);
		IBlockState state1 = world.getBlockState(pos1);

		if(!world.isBlockModifiable(context.caster, pos) || !world.isBlockModifiable(context.caster, pos1))
			return null;
		
		if(world.isAirBlock(pos1) || state1.getBlock().isReplaceable(world, pos1)) {
			world.setBlockState(pos1, state, 1 | 2);
			world.setBlockToAir(pos);
			world.playEvent(2001, pos, Block.getStateId(state));
		}

		return null;
	}

}
