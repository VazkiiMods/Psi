/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityLook extends PieceOperator {

	SpellParam<Entity> target;

	public PieceOperatorEntityLook(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity e = this.getParamValue(context, target);

		if(e == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		if(e instanceof Projectile || e instanceof FallingBlockEntity) {
			return new Vector3(e.getDeltaMovement());
		}

		return new Vector3(e.getViewVector(1F));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
