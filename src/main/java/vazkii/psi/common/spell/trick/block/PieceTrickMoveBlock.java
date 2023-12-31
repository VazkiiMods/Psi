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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickMoveBlock extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Vector3> target;

	public PieceTrickMoveBlock(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(10));
		setStatLabel(EnumSpellStat.COST, new StatLabel(15));
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
		ItemStack tool = context.getHarvestTool();
		Vector3 positionVal = this.getParamValue(context, position);
		Vector3 targetVal = this.getParamValue(context, target);

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		Level world = context.focalPoint.getCommandSenderWorld();
		BlockPos pos = positionVal.toBlockPos();

		/**
		 * TODO: Find a better solution than this bandaid for block duping (see #740)
		 * A possible solution is moving this logic to {@link PieceTrickBreakBlock}
		 * As well as passing the spell context to it as a parameter. The Spell Context would need to have a way to
		 * check if it has been delayed or not
		 * Since there are legitimate use cases besides duping when you want to move a block that is in the same
		 * position that you previously had broken.
		 */
		if(context.positionBroken != null && context.positionBroken.getBlockPos().equals(pos)) {
			return null;
		}
		BlockState state = world.getBlockState(pos);
		if(world.getBlockEntity(pos) != null || state.getPistonPushReaction() != PushReaction.NORMAL ||
				state.getDestroySpeed(world, pos) == -1 ||
				!PieceTrickBreakBlock.canHarvestBlock(state, context.caster, world, pos, tool)) {
			return null;
		}

		BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, world, pos, tool);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled()) {
			return null;
		}

		if(!targetVal.isAxial() || targetVal.isZero()) {
			return null;
		}

		Vector3 axis = targetVal.normalize();
		int x = pos.getX() + (int) axis.x;
		int y = pos.getY() + (int) axis.y;
		int z = pos.getZ() + (int) axis.z;
		BlockPos pos1 = new BlockPos(x, y, z);
		BlockState state1 = world.getBlockState(pos1);

		if(!world.mayInteract(context.caster, pos) || !world.mayInteract(context.caster, pos1)) {
			return null;
		}

		if(state1.isAir() || state1.getMaterial().isReplaceable()) {
			world.setBlock(pos1, state, 1 | 2);
			world.removeBlock(pos, false);
			world.levelEvent(2001, pos, Block.getId(state));
		}

		return null;
	}

}
