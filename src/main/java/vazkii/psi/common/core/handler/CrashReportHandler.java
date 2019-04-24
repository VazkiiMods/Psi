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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.ICrashCallable;
import vazkii.psi.api.spell.CompiledSpell;

public class CrashReportHandler implements ICrashCallable {
	private static ThreadLocal<CompiledSpell> activeSpell = new ThreadLocal<>();

	public static void setSpell(CompiledSpell spell) {
		activeSpell.set(spell);
	}

	@Override
	public String getLabel() {
		return "[Psi] Active spell";
	}

	@Override
	public String call() {
		CompiledSpell spell = activeSpell.get();
		if (spell == null)
			return "None";

		NBTTagCompound result = new NBTTagCompound();
		spell.sourceSpell.writeToNBT(result);
		return result.toString();
	}
}
