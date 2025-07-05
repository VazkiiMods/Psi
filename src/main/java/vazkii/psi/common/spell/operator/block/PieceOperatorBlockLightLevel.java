/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.block;

import net.minecraft.core.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorBlockLightLevel extends PieceOperator {

	SpellParam<Vector3> target;

	public PieceOperatorBlockLightLevel(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos pos = SpellHelpers.getBlockPos(this, context, target, false, false);
		int j = context.focalPoint.level().getMaxLocalRawBrightness(pos);
		return j * 1.0;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
