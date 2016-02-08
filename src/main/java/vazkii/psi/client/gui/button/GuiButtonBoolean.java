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
import vazkii.psi.client.gui.GuiIntroduction;

public class GuiButtonBoolean extends GuiButton {

	public boolean yes;

	public GuiButtonBoolean(int par2, int par3, boolean yes) {
		super(0, par2, par3, 12, 11, "");
		this.yes = yes;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(enabled) {
			hovered = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiIntroduction.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(xPosition, yPosition, yes ? 0 : 12, k == 2 ? 184 : 195, width, height);
		}
	}

}
