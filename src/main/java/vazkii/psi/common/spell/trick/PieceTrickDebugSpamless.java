/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageSpamlessChat;

public class PieceTrickDebugSpamless extends PieceTrick {

	SpellParam<SpellParam.Any> target;
	SpellParam<Number> number;

	public PieceTrickDebugSpamless(Spell spell) {
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

		ITextComponent component = new StringTextComponent(String.valueOf(targetVal));
		if (numberVal != null) {
			String numStr = "" + numberVal;
			if (numberVal.doubleValue() - numberVal.intValue() == 0) {
				int numInt = numberVal.intValue();
				numStr = "" + numInt;
			}

			component = new StringTextComponent("[" + numStr + "]")
					.setStyle(Style.EMPTY.withColor(TextFormatting.AQUA))
					.append(new StringTextComponent(" ")
							.setStyle(Style.EMPTY.withColor(TextFormatting.RESET)))
					.append(component.plainCopy().setStyle(Style.EMPTY.withColor(TextFormatting.RESET)));
		}

		if (context.caster instanceof ServerPlayerEntity) {
			MessageSpamlessChat chatMessage = new MessageSpamlessChat(component, numberVal == null ? -1 : numberVal.intValue());
			MessageRegister.sendToPlayer(chatMessage, context.caster);
		}

		return null;
	}
}
