/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [24/01/2016, 15:03:19 (GMT)]
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityAxialLook extends PieceOperator {

	SpellParam target;

	public PieceOperatorEntityAxialLook(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity e = this.getParamValue(context, target);

		if(e == null)
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		Vec3d look = e.getLook(1F);
		Direction facing = Direction.getFacingFromVector((float) look.x, (float) look.y, (float) look.z);

		return new Vector3(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
