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
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.client.gui.GuiProgrammer;

import javax.annotation.Nonnull;

public class GuiButtonPage extends Button {

	public final boolean right;
	final GuiProgrammer gui;

	public GuiButtonPage(GuiProgrammer gui, int par2, int par3, boolean right) {
		super(0, par2, par3, 18, 10, "");
		this.right = right;
		this.gui = gui;
	}

	@Override
	public void drawButton(@Nonnull Minecraft par1Minecraft, int par2, int par3, float pticks) {
		if(enabled) {
			hovered = par2 >= x && par3 >= y && par2 < x + width && par3 < y + height;
			int k = getHoverState(hovered);

			par1Minecraft.textureManager.bindTexture(GuiProgrammer.texture);
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			blit(x, y, k == 2 ? 216 : 198, right ? 145 : 155, width, height);

			if(k == 2)
				gui.tooltip.add(TooltipHelper.local(right ? "psimisc.nextPage" : "psimisc.prevPage"));
		}
	}

}
