/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.helpers.SpellHelpers;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

public class PieceTrickDetonate extends PieceTrick {

	SpellParam<Number> radius;

	public PieceTrickDetonate(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		double radiusVal = Math.min(32, SpellHelpers.ensurePositiveOrZero(this, radius));
		meta.addStat(EnumSpellStat.POTENCY, (int) Math.min(radiusVal, 5));
		meta.addStat(EnumSpellStat.COST, (int) Math.ceil(radiusVal * 5));

		if (radiusVal == 0) {
			meta.addStat(EnumSpellStat.COMPLEXITY, 1);
		}
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double radiusVal = Math.min(MAX_DISTANCE, this.getNonnullParamValue(context, radius).doubleValue());

		if (radiusVal == 0.0) {
			IDetonationHandler.performDetonation(context.caster.world, context.caster, 0, entity -> entity == context.caster);
			return null;
		}
		IDetonationHandler.performDetonation(context.caster.world, context.caster, radiusVal);
		return null;
	}
}
