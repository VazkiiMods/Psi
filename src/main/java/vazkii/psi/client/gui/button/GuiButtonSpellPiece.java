/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 17:35:40 (GMT)]
 */
package vazkii.psi.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

import javax.annotation.Nonnull;

public class GuiButtonSpellPiece extends Button {

	public final SpellPiece piece;
	final GuiProgrammer gui;

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y) {
		super(0, x, y, 16, 16, "");
		this.gui = gui;
		this.piece = piece;
	}

	@Override
	public void drawButton(@Nonnull Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
		if(enabled && visible) {
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			int i = getHoverState(hovered);

			GlStateManager.pushMatrix();
			GlStateManager.color3f(1F, 1F, 1F);
			GlStateManager.translatef(x, y, 0);
			piece.draw();
			GlStateManager.popMatrix();

			minecraft.getTextureManager().bindTexture(GuiProgrammer.texture);

			if (i == 2)
				blit(x, y, 16, gui.ySize, 16, 16);

			if(i == 2)
				piece.getTooltip(gui.tooltip);
		}
	}

}
