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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonIO extends GuiButton {

	public boolean out;
	GuiProgrammer gui;

	public GuiButtonIO(GuiProgrammer gui, int par2, int par3, boolean out) {
		super(0, par2, par3, 12, 12, "");
		this.out = out;
		this.gui = gui;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(enabled) {
			hovered = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiProgrammer.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(xPosition, yPosition, k == 2 ? 186 : 174, out ? 169 : 181, width, height);

			if(k == 2) {
				gui.tooltip.add((out ? EnumChatFormatting.RED : EnumChatFormatting.BLUE) + StatCollector.translateToLocal(out ? "psimisc.exportToClipboard" : "psimisc.importFromClipboard"));
				gui.tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("psimisc.mustHoldShift"));
			}
		}
	}

}
