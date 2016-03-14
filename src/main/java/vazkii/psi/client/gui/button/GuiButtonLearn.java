/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 16, 2014, 4:52:06 PM (GMT)]
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import vazkii.psi.client.gui.GuiLeveling;

public class GuiButtonLearn extends GuiButton {

	GuiLeveling gui;

	public GuiButtonLearn(GuiLeveling gui, int par2, int par3) {
		super(0, par2, par3, 26, 22, "");
		this.gui = gui;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(enabled) {
			hovered = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiLeveling.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(xPosition, yPosition, k == 2 ? 44 : 18, 184, width, height);

			if(k == 2)
				gui.tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("psimisc.learn"));
		}
	}

}