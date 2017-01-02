/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [10/03/2016, 19:48:45 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorBlockPresence extends PieceSelector {

	SpellParam position;

	public PieceSelectorBlockPresence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.<Vector3>getParamValue(context, position);

		if(positionVal == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		
		BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
		IBlockState state = context.caster.getEntityWorld().getBlockState(pos);
		Block block = state.getBlock();
		
		if(block.isAir(state, context.caster.getEntityWorld(), pos) || block.isReplaceable(context.caster.getEntityWorld(), pos))
			return 0.0;
		else if(state.getBoundingBox(context.caster.getEntityWorld(), pos).offset(pos) == null)
			return 1.0;
		return 2.0;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
