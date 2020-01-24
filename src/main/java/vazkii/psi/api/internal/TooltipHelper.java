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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public final class TooltipHelper {

    @OnlyIn(Dist.CLIENT)
    public static void tooltipIfShift(List<ITextComponent> tooltip, Runnable r) {
        if (Screen.hasShiftDown())
            r.run();
        else addToTooltip(tooltip, "psimisc.shiftForInfo");
    }

    @OnlyIn(Dist.CLIENT)
    public static void addToTooltip(List<ITextComponent> tooltip, String s, Object... format) {
        tooltip.add(local(s, format));
        //TODO check if the lack of .replaceAll("&", "\u00a7")) is okay
    }

    // todo check if okay
    public static TranslationTextComponent local(String s, Object... format) {
        return new TranslationTextComponent(s, format);
    }

    public static TranslationTextComponent local(String s) {
        return local(s, new Object[0]);
    }

}
