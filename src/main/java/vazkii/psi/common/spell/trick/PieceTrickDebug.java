/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDebug extends PieceTrick {

	SpellParam<SpellParam.Any> target;
	SpellParam<Number> number;

	public PieceTrickDebug(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, true, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) {
		// NO-OP
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Number numberVal = this.getParamValue(context, number);
		Object targetVal = getParamValue(context, target);

		Component component = new TextComponent(String.valueOf(targetVal));

		if (numberVal != null) {
			String numStr = "" + numberVal;
			if (numberVal.doubleValue() - numberVal.intValue() == 0) {
				int numInt = numberVal.intValue();
				numStr = "" + numInt;
			}

			component = new TextComponent("[" + numStr + "]")
					.setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))
					.append(new TextComponent(" ")
							.setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)))
					.append(component);
		}

		context.caster.sendMessage(component, Util.NIL_UUID);

		return null;
	}

}
