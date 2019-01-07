/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [03/02/2016, 18:10:29 (GMT)]
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.gui.GuiProgrammer;

import javax.annotation.Nonnull;

public class GuiButtonIO extends GuiButton {

	public boolean out;
	GuiProgrammer gui;

	public GuiButtonIO(GuiProgrammer gui, int par2, int par3, boolean out) {
		super(0, par2, par3, 12, 12, "");
		this.out = out;
		this.gui = gui;
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float pticks) {
		if(enabled && !gui.takingScreenshot) {
			hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiProgrammer.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(x, y, k == 2 ? 186 : 174, out ? 169 : 181, width, height);

			if(k == 2) {
				gui.tooltip.add((out ? TextFormatting.RED : TextFormatting.BLUE) + TooltipHelper.local(out ? "psimisc.exportToClipboard" : "psimisc.importFromClipboard"));
				gui.tooltip.add(TextFormatting.GRAY + TooltipHelper.local("psimisc.mustHoldShift"));
			}
		}
	}

}
