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

import net.minecraft.client.gui.widget.button.Button;

public class GuiButtonPage extends Button {
    public GuiButtonPage(int p_i51141_1_, int p_i51141_2_, int p_i51141_3_, int p_i51141_4_, String p_i51141_5_, IPressable p_i51141_6_) {
        super(p_i51141_1_, p_i51141_2_, p_i51141_3_, p_i51141_4_, p_i51141_5_, p_i51141_6_);
    }
	/*
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
	}*/

}
