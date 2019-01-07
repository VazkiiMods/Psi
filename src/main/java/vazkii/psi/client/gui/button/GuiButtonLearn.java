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
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.gui.GuiLeveling;

import javax.annotation.Nonnull;

public class GuiButtonLearn extends GuiButton {

	GuiLeveling gui;

	public GuiButtonLearn(GuiLeveling gui, int par2, int par3) {
		super(0, par2, par3, 26, 22, "");
		this.gui = gui;
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float pticks) {
		if(enabled) {
			hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
			int k = getHoverState(hovered);

			par1Minecraft.renderEngine.bindTexture(GuiLeveling.texture);
			GlStateManager.color(1F, 1F, 1F, 1F);
			drawTexturedModalRect(x, y, k == 2 ? 44 : 18, 184, width, height);

			if(k == 2)
				gui.tooltip.add(TextFormatting.GREEN + TooltipHelper.local("psimisc.learn"));
		}
	}

}
