/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.internal;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public final class TooltipHelper {

	@OnlyIn(Dist.CLIENT)
	public static void tooltipIfShift(List<Component> tooltip, Runnable r) {
		if(Screen.hasShiftDown()) {
			r.run();
		} else {
			tooltip.add(new TranslatableComponent("psimisc.shift_for_info"));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void tooltipIfCtrl(List<Component> tooltip, Runnable r) {
		if(Screen.hasControlDown()) {
			r.run();
		} else {
			tooltip.add(new TranslatableComponent("psimisc.ctrl_for_stats"));
		}
	}

}
