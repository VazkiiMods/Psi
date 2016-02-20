/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [19/02/2016, 17:51:29 (GMT)]
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

public class PieceTrickConjureBlockSequence extends PieceTrick {

	SpellParam position;
	SpellParam target;
	SpellParam maxBlocks;
	SpellParam timeVal;

	public PieceTrickConjureBlockSequence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
		addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
		addParam(timeVal = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
		if(maxBlocksVal == null || maxBlocksVal <= 0)
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 15));
		meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 20));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);
		Vector3 targetVal = this.<Vector3>getParamValue(context, target);
		Double maxBlocksVal = this.<Double>getParamValue(context, maxBlocks);
		Double timeVal = this.<Double>getParamValue(context, time);
		int maxBlocksInt = maxBlocksVal.intValue();

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

		int len = (int) targetVal.mag();
		Vector3 targetNorm = targetVal.copy().normalize();
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		
		for(int i = 0; i < Math.min(len, maxBlocksInt); i++) {
			Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

			if(!context.isInRadius(blockVec))
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

			BlockPos pos = new BlockPos(blockVec.x, blockVec.y, blockVec.z);
			PieceTrickPlaceBlock.placeBlock(context.caster, context.caster.worldObj, pos, false, true);
			IBlockState state = context.caster.worldObj.getBlockState(pos);
			
			if(!context.caster.worldObj.isRemote && state.getBlock() == ModBlocks.conjured) {
				context.caster.worldObj.setBlockState(pos, messWithState(state));
				TileConjured tile = (TileConjured) context.caster.worldObj.getTileEntity(pos);

				if(timeVal != null && timeVal.intValue() > 0) {
					int val = timeVal.intValue();
					tile.time = val * 20;
				}
				
				if(cad != null)
					tile.colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
			}
			
		}

		return null;
	}
	
	public IBlockState messWithState(IBlockState state) {
		return state.withProperty(BlockConjured.SOLID, true);
	}

}
