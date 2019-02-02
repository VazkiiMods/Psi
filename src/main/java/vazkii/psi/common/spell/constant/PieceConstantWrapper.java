/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [19/02/2016, 19:11:22 (GMT)]
 */
package vazkii.psi.common.spell.constant;

import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;

public class PieceConstantWrapper extends SpellPiece {

	SpellParam target;
	SpellParam max;
	
	boolean evaluating = false;

	public PieceConstantWrapper(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
		addParam(max = new ParamNumber("psi.spellparam.constant", SpellParam.GREEN, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}

	@Override
	public Object execute(SpellContext context) {
		Double targetVal = this.<Double>getParamValue(context, target);
		Double maxVal = this.<Double>getParamValue(context, max);

		if(maxVal > 0)
			return Math.min(maxVal, Math.abs(targetVal));
		else if(maxVal < 0)
			return Math.max(maxVal, -Math.abs(targetVal));
		else return 0.0;
	}

	@Override
	public Object evaluate() throws SpellCompilationException {
		if(evaluating)
			return 0.0;
		
		evaluating = true;
		Object ret = getParamEvaluation(max);
		evaluating = false;
		
		return ret;
	}

	@Override
	public EnumPieceType getPieceType() {
		return EnumPieceType.CONSTANT;
	}

	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}

}
