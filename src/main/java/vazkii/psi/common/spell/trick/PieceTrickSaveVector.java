/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [10/03/2016, 23:03:43 (GMT)]
 */
package vazkii.psi.common.spell.trick;

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
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSaveVector extends PieceTrick {
	
	public static final String KEY_SLOT_LOCKED = "psi:SlotLocked";
	
	SpellParam<Number> number;
	SpellParam<Vector3> target;

	public PieceTrickSaveVector(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
		addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);

		Double numberVal = this.<Double>getParamEvaluation(number);
		if(numberVal == null || numberVal <= 0 || numberVal != numberVal.intValue())
			throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

		meta.addStat(EnumSpellStat.POTENCY, numberVal.intValue() * 8);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Number numberVal = this.getParamValue(context, number).doubleValue();
		Vector3 targetVal = this.getParamValue(context, target);

		int n = numberVal.intValue() - 1;

		if(context.customData.containsKey(KEY_SLOT_LOCKED + n))
			return null;
		
		ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
		if(cadStack == null || !(cadStack.getItem() instanceof ICAD))
			throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
		ICAD cad = (ICAD) cadStack.getItem();
		cad.setStoredVector(cadStack, n, targetVal);
		
		context.customData.put(KEY_SLOT_LOCKED + n, 0);
		
		return null;
	}

}
