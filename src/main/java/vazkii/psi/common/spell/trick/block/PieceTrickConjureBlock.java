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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.StatLabel;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileConjured;

import javax.annotation.Nullable;

public class PieceTrickConjureBlock extends PieceTrick {

	SpellParam<Vector3> position;
	SpellParam<Number> time;

	public PieceTrickConjureBlock(Spell spell) {
		super(spell);
		setStatLabel(EnumSpellStat.POTENCY, new StatLabel(15));
		setStatLabel(EnumSpellStat.COST, new StatLabel(20));
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

	public void addStats(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.POTENCY, 15);
		meta.addStat(EnumSpellStat.COST, 20);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);
		Number timeVal = this.getParamValue(context, time);

		if(positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		if(!context.isInRadius(positionVal)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}

		BlockPos pos = positionVal.toBlockPos();

		Level world = context.focalPoint.getCommandSenderWorld();

		if(!world.mayInteract(context.caster, pos)) {
			return null;
		}

		conjure(context, timeVal, pos, world, messWithState(ModBlocks.conjured.defaultBlockState()));

		return null;
	}

	public static void conjure(SpellContext context, @Nullable Number timeVal, BlockPos pos, Level world, BlockState state) {
		if(world.getBlockState(pos).getBlock() != state.getBlock()) {
			if(conjure(world, pos, context.caster, state)) {
				if(timeVal != null && timeVal.intValue() > 0) {
					int val = timeVal.intValue();
					world.scheduleTick(pos, state.getBlock(), val);
				}

				BlockEntity tile = world.getBlockEntity(pos);

				ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
				if(tile instanceof TileConjured && !cad.isEmpty()) {
					((TileConjured) tile).colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				}

			}
		}
	}

	public static boolean conjure(Level world, BlockPos pos, Player player, BlockState state) {
		if(!world.hasChunkAt(pos) || !world.mayInteract(player, pos)) {
			return false;
		}

		BlockState inWorld = world.getBlockState(pos);
		if(inWorld.isAir() || inWorld.canBeReplaced()) {
			return world.setBlockAndUpdate(pos, state);
		}
		return false;
	}

	public BlockState messWithState(BlockState state) {
		return state.setValue(BlockConjured.SOLID, true);
	}

}
