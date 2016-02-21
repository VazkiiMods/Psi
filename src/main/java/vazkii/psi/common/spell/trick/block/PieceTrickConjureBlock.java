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

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.block.BlockConjured;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.block.tile.TileConjured;

public class PieceTrickConjureBlock extends PieceTrick {

	SpellParam position;
	SpellParam time;

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
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);
		Double timeVal = this.<Double>getParamValue(context, time);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
		PieceTrickPlaceBlock.placeBlock(context.caster, context.caster.worldObj, pos, false, true);
		IBlockState state = context.caster.worldObj.getBlockState(pos);

		if(!context.caster.worldObj.isRemote && state.getBlock() == ModBlocks.conjured) {
			context.caster.worldObj.setBlockState(pos, messWithState(state));
			TileConjured tile = (TileConjured) context.caster.worldObj.getTileEntity(pos);

			if(timeVal != null && timeVal.intValue() > 0) {
				int val = timeVal.intValue();
				tile.time = val * 20;
			}

			ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
			if(cad != null)
				tile.colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
		}

		return null;
	}

	public IBlockState messWithState(IBlockState state) {
		return state.withProperty(BlockConjured.SOLID, true);
	}

}
