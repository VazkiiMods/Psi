/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [16/01/2016, 16:13:25 (GMT)]
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDebug extends PieceTrick {

	SpellParam target;
	SpellParam number;
	
	public PieceTrickDebug(Spell spell) {
		super(spell);
	}
	
	@Override
	public void initParams() {
		addParam(target = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, true, true));
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		// NO-OP
	}
	
	@Override
	public Object execute(SpellContext context) {
		if(context.caster.worldObj.isRemote)
			return null;
			
		Double numberVal = this.<Double>getParamValue(context, number);
		Object targetVal = getParamValue(context, target);
		
		String s = targetVal.toString();
		if(numberVal != null) {
			String numStr = "" + numberVal;
			if(numberVal - numberVal.intValue() == 0) {
				int numInt = numberVal.intValue();
				numStr = "" + numInt;
			}
			
			s = EnumChatFormatting.AQUA + "[" + numStr + "] " + EnumChatFormatting.RESET + s;
		}
		
		context.caster.addChatMessage(new ChatComponentText(s));
		
		return null;
	}


}
