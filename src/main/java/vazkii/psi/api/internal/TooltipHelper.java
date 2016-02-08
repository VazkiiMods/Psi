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

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class TooltipHelper {

	public static void tooltipIfShift(List<String> tooltip, Runnable r) {
		if(GuiScreen.isShiftKeyDown())
			r.run();
		else addToTooltip(tooltip, "psimisc.shiftForInfo");
	}
	
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		s = local(s).replaceAll("&", "\u00a7");
		
		Object[] formatVals = new String[format.length];
		for(int i = 0; i < format.length; i++)
			formatVals[i] = local(format[i].toString()).replaceAll("&", "\u00a7");
		
		if(formatVals != null && formatVals.length > 0)
			s = String.format(s, formatVals);
		
		tooltip.add(s);
	}
	
	public static String local(String s) {
		return StatCollector.translateToLocal(s);
	}
	
}
