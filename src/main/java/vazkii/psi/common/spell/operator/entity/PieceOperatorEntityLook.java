/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;

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

		if (e == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
		}
		if (e instanceof IProjectile || e instanceof DamagingProjectileEntity || e instanceof FallingBlockEntity) {
			return new Vector3(e.getMotion());
		}

		return new Vector3(e.getLook(1F));
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
