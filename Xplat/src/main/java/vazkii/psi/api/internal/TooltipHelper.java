/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.network.chat.Component;

import vazkii.psi.common.client.PsiClientRuntime;

import java.util.List;

public final class TooltipHelper {

	public static void tooltipIfShift(List<Component> tooltip, Runnable r) {
		if(PsiClientRuntime.hasShiftDown()) {
			r.run();
		} else {
			tooltip.add(Component.translatable("psimisc.shift_for_info"));
		}
	}

	public static void tooltipIfCtrl(List<Component> tooltip, Runnable r) {
		if(PsiClientRuntime.hasControlDown()) {
			r.run();
		} else {
			tooltip.add(Component.translatable("psimisc.ctrl_for_stats"));
		}
	}

}
