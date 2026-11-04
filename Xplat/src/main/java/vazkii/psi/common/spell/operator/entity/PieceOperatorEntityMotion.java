/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.handler.PlayerData;
import vazkii.psi.common.platform.PsiPlayerDataStorage;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;

public class PieceOperatorEntityMotion extends PieceOperator {

	SpellParam<Entity> target;

	public PieceOperatorEntityMotion(Spell spell) {
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

		return getRawMotion(context, e).multiply(1.0 / PieceTrickAddMotion.MULTIPLIER);
	}

	public static Vector3 getRawMotion(SpellContext context, Entity e) {
		if(!(e instanceof Player player) || (e.level().isClientSide && e == context.caster)) {
			return new Vector3(e.getDeltaMovement());
		}

		PlayerData data = PsiPlayerDataStorage.get(player);
		if(data.eidosChangelog.size() >= 2) {
			Vector3 last = data.eidosChangelog.get(data.eidosChangelog.size() - 2);
			Vector3 vec = Vector3.fromEntity(e).sub(last);
			if(vec.mag() < 10 * PieceTrickAddMotion.MULTIPLIER) {
				return vec;
			}
		}

		return new Vector3(e.getDeltaMovement());
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
