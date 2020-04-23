/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.helpers.SpellHelpers;

public class PieceTrickChangeSlot extends PieceTrick {

	SpellParam<Number> slot;

	public PieceTrickChangeSlot(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		double slt = SpellHelpers.ensurePositiveOrZero(this, slot);
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public void initParams() {
		addParam(slot = new ParamNumber(SpellParam.GENERIC_NAME_SLOT, SpellParam.RED, false, true));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		int slt = this.getParamValue(context, slot).intValue();
		context.customTargetSlot = true;
		context.targetSlot = slt;
		return null;
	}

}
