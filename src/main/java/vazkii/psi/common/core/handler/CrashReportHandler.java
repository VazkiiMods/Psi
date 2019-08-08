/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Apr 24, 2019, 15:49 AM (EST)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ICrashCallable;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.SpellPiece;

public class CrashReportHandler implements ICrashCallable {
	private static ThreadLocal<CompiledSpell> activeSpell = new ThreadLocal<>();
	private static ThreadLocal<SpellPiece> activePiece = new ThreadLocal<>();

	public static void setSpell(CompiledSpell spell, SpellPiece piece) {
		activeSpell.set(spell);
		activePiece.set(piece);
	}

	@Override
	public String getLabel() {
		return "[Psi] Active spell";
	}

	@Override
	public String call() {
		CompiledSpell spell = activeSpell.get();
		SpellPiece piece = activePiece.get();
		if (spell == null)
			return "None";

		String prefix = "";
		if (piece != null)
			prefix =  "[" + piece.x + ", " + piece.y + "] in ";

		CompoundNBT result = new CompoundNBT();
		spell.sourceSpell.writeToNBT(result);
		return prefix + result;
	}
}
