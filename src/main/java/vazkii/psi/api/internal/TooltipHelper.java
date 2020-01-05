/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:30:59 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.PsiAPI;

import java.util.List;

public final class TooltipHelper {

	@OnlyIn(Dist.CLIENT)
	public static void tooltipIfShift(List<String> tooltip, Runnable r) {
		if(Screen.hasShiftDown())
			r.run();
		else addToTooltip(tooltip, "psimisc.shiftForInfo");
	}

	@OnlyIn(Dist.CLIENT)
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		tooltip.add(local(s, format).replaceAll("&", "\u00a7"));
	}

	// todo 1.14 replace these with textcomponentranslation or direct calls to i18n.format
	public static String local(String s, Object... format) {
		return PsiAPI.internalHandler.localize(s, format);
	}

	public static String local(String s) {
		return local(s, new Object[0]);
	}

}
