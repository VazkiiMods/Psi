/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.ISystemReportExtender;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.SpellPiece;

public class CrashReportHandler implements ISystemReportExtender {
	private static final ThreadLocal<CompiledSpell> activeSpell = new ThreadLocal<>();
	private static final ThreadLocal<SpellPiece> activePiece = new ThreadLocal<>();

	public static void setSpell(CompiledSpell spell, SpellPiece piece) {
		activeSpell.set(spell);
		activePiece.set(piece);
	}

	@Override
	public String getLabel() {
		return "[Psi] Active spell";
	}

	@Override
	public String get() {
		CompiledSpell spell = activeSpell.get();
		SpellPiece piece = activePiece.get();
		if (spell == null) {
			return "None";
		}

		String prefix = "";
		if (piece != null) {
			prefix = "[" + piece.x + ", " + piece.y + "] in ";
		}

		CompoundTag result = new CompoundTag();
		spell.sourceSpell.writeToNBT(result);
		return prefix + result;
	}
}
