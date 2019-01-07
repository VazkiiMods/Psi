/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [420/02/2016, 18:12:57 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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

public class PieceTrickBlaze extends PieceTrick {

	SpellParam position;

	public PieceTrickBlaze(Spell spell) {
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
		meta.addStat(EnumSpellStat.COST, 40);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.getEntityWorld().isRemote)
			return null;

		Vector3 positionVal = this.getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		if(!context.isInRadius(positionVal))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		BlockPos pos = positionVal.toBlockPos();

		pos = pos.down();
		IBlockState state = context.caster.getEntityWorld().getBlockState(pos);
		if(state.getBlock().isAir(state, context.caster.getEntityWorld(), pos) || state.getBlock().isReplaceable(context.caster.getEntityWorld(), pos))
			context.caster.getEntityWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
		else {
			pos = pos.up();
			state = context.caster.getEntityWorld().getBlockState(pos);
			if(state.getBlock().isAir(state, context.caster.getEntityWorld(), pos) || state.getBlock().isReplaceable(context.caster.getEntityWorld(), pos))
				context.caster.getEntityWorld().setBlockState(pos, Blocks.FIRE.getDefaultState());
		}

		return null;
	}

}
