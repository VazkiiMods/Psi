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
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickRussianRoulette extends PieceTrick {
	public PieceTrickRussianRoulette(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
	}

	public static int getRandomSocketableSlot(ISocketable socketable) {
		int maxSlots;
		for (maxSlots = 0; !socketable.isSocketSlotAvailable(maxSlots); maxSlots++) ;
		return (int) ((Math.random() * (maxSlots + 1)));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {

		if (!context.tool.isEmpty() || context.castFrom == null || context.focalPoint != context.caster) {
			throw new SpellRuntimeException(SpellRuntimeException.CAD_CASTING_ONLY);
		}

		ItemStack stack = context.tool.isEmpty() ? PsiAPI.getPlayerCAD(context.caster) : context.tool;
		ISocketable capability = stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);

		int targetSlot = getRandomSocketableSlot(capability);

		capability.setSelectedSlot(targetSlot);
		return null;
	}

}
