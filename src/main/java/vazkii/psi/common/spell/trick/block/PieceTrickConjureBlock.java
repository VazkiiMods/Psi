/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [17/02/2016, 18:35:25 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileConjured;

public class PieceTrickConjureBlock extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Number> time;

	public PieceTrickConjureBlock(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		addStats(meta);
	}

	public void addStats(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 15);
		meta.addStat(EnumSpellStat.COST, 20);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		double timeVal = this.getParamValue(context, time).doubleValue();

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		
		BlockPos pos = positionVal.toBlockPos();

		World world = context.caster.getEntityWorld();

		if(!world.isBlockModifiable(context.caster, pos))
			return null;

		conjure(context, timeVal, pos, world, messWithState(ModBlocks.conjured.getDefaultState()));

		return null;
	}

	public static void conjure(SpellContext context, Double timeVal, BlockPos pos, World world, BlockState state) {
		if(world.getBlockState(pos).getBlock() != state.getBlock()) {
			if (conjure(world, pos, context.caster, state)) {
				if (timeVal != null && timeVal.intValue() > 0) {
					int val = timeVal.intValue();
					world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), val);
				}

				TileEntity tile = world.getTileEntity(pos);

				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				if (tile instanceof TileConjured && !cad.isEmpty()) {
					((TileConjured) tile).colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				}

			}
		}
	}

	public static boolean conjure(World world, BlockPos pos, PlayerEntity player, BlockState state) {
		if(!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos))
			return false;

		BlockState inWorld = world.getBlockState(pos);
		if(inWorld.isAir(world, pos) || inWorld.getMaterial().isReplaceable())
			return world.setBlockState(pos, state);
		return false;
	}

	public BlockState messWithState(BlockState state) {
		return state.with(BlockConjured.SOLID, true);
	}

}
