/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [10/03/2016, 23:27:24 (GMT)]
 */
package vazkii.psi.common.spell.selector;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.spell.trick.PieceTrickSaveVector;

public class PieceSelectorSavedVector extends PieceSelector {

	SpellParam number;

	public PieceSelectorSavedVector(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		Double numberVal = this.<Double>getParamEvaluation(number);
		if(numberVal == null || numberVal <= 0 || numberVal.doubleValue() != numberVal.intValue())
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

		meta.addStat(EnumSpellStat.POTENCY, numberVal.intValue() * 6);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Double numberVal = this.<Double>getParamValue(context, number);
		
		int n = numberVal.intValue();
		if(context.customData.containsKey(PieceTrickSaveVector.KEY_SLOT_LOCKED + n))
			throw new SpellRuntimeException(SpellRuntimeException.LOCKED_MEMORY);
		
		ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
		if(cadStack == null || !(cadStack.getItem() instanceof ICAD))
			throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
		ICAD cad = (ICAD) cadStack.getItem();
		return cad.getStoredVector(cadStack, n);
	}

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
