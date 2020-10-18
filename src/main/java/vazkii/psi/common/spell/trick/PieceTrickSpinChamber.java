/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.trick;

import net.minecraft.item.ItemStack;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.item.ItemCAD;

public class PieceTrickSpinChamber extends PieceTrick {
	private SpellParam<Number> number;

	public PieceTrickSpinChamber(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, 2);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double num = this.getParamValue(context, number).doubleValue();

		if (num == 0) {
			return null;
		}

		if (!context.tool.isEmpty() || context.castFrom == null || context.focalPoint != context.caster) {
			throw new SpellRuntimeException(SpellRuntimeException.CAD_CASTING_ONLY);
		}

		ItemStack inHand = context.caster.getHeldItem(context.castFrom);

		if (inHand.isEmpty() || !(inHand.getItem() instanceof ICAD) || !inHand.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).isPresent()) {
			throw new SpellRuntimeException(SpellRuntimeException.CAD_CASTING_ONLY);
		}

		ItemStack stack = PsiAPI.getPlayerCAD(context.caster);
		ISocketable capability = inHand.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);
		ItemCAD cad = (ItemCAD) stack.getItem();

		int selectedSlot = capability.getSelectedSlot();
		int sockets = cad.getStatValue(stack, EnumCADStat.SOCKETS);

		int offset = num > 0 ? 1 : -1;

		int target = ((selectedSlot + offset) + sockets) % sockets;

		capability.setSelectedSlot(target);

		return null;
	}
}
