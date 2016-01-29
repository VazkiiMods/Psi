/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [29/01/2016, 17:23:57 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceTrickMassBlink extends PieceTrick {

	SpellParam target;
	SpellParam distance;
	
	public PieceTrickMassBlink(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(target = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(distance = new ParamNumber("psi.spellparam.distance", SpellParam.RED, false, true));
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double distanceVal = this.<Double>getParamEvaluation(distance);
		if(distanceVal == null)
			distanceVal = 1D;
		
		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 80));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 100));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper targetVal = this.<EntityListWrapper>getParamValue(context, target);
		Double distanceVal = this.<Double>getParamValue(context, distance);

		for(Entity e : targetVal)
			PieceTrickBlink.blink(context, e, distanceVal);
		
		return null;
	}
	
}
