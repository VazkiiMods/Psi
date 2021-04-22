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
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.item.ItemCAD;

public class PieceTrickRussianRoulette extends PieceTrick {
	public PieceTrickRussianRoulette(Spell spell) {
		super(spell);
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {

		if (!context.tool.isEmpty() || context.castFrom == null || context.focalPoint != context.caster) {
			throw new SpellRuntimeException(SpellRuntimeException.CAD_CASTING_ONLY);
		}

		ItemStack stack = context.tool.isEmpty() ? PsiAPI.getPlayerCAD(context.caster) : context.tool;
		ISocketable capability = stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseThrow(NullPointerException::new);
		ItemCAD cad = (ItemCAD) stack.getItem();

		int sockets = cad.getStatValue(stack, EnumCADStat.SOCKETS);
		int target = (int) ((Math.random() * sockets));

		capability.setSelectedSlot(target);
		return null;
	}
}
