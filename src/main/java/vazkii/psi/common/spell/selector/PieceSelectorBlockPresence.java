/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorBlockPresence extends PieceSelector {

	SpellParam<Vector3> position;

	public PieceSelectorBlockPresence(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = this.getParamValue(context, position);

		if (positionVal == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}

		BlockPos pos = positionVal.toBlockPos();
		BlockState state = context.caster.getCommandSenderWorld().getBlockState(pos);
		Block block = state.getBlock();

		if (state.isAir() || state.getMaterial().isReplaceable()) {
			return 0.0;
		} else if (state.getCollisionShape(context.caster.getCommandSenderWorld(), pos, CollisionContext.of(context.caster)).isEmpty()) {
			return 1.0;
		}
		return 2.0;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
