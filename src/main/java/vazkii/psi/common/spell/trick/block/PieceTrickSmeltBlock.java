/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 18:08:02 (GMT)]
 */
package vazkii.psi.common.spell.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbySmeltables;

public class PieceTrickSmeltBlock extends PieceTrick {

	SpellParam position;

	public PieceTrickSmeltBlock(Spell spell) {
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
	@SuppressWarnings("deprecation")
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = positionVal.toBlockPos();
		if(!context.caster.getEntityWorld().isBlockModifiable(context.caster, pos))
			return null;
		
		BlockState state = context.caster.getEntityWorld().getBlockState(pos);
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(block);
		ItemStack result = PieceSelectorNearbySmeltables.simulateSmelt(context.caster.getEntityWorld(), stack);
		if(!result.isEmpty()) {
			Item item = result.getItem();
			Block block1 = Block.getBlockFromItem(item);
			if(block1 != Blocks.AIR) {
				context.caster.getEntityWorld().setBlockState(pos, block1.getDefaultState());
				context.caster.getEntityWorld().playEvent(2001, pos, Block.getStateId(block1.getDefaultState()));
			}
		}

		return null;
	}

}
