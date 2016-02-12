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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSpellPiece extends GuiButton {

	public SpellPiece piece;
	GuiProgrammer gui;

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y) {
		super(0, x, y, 16, 16, "");
		this.gui = gui;
		this.piece = piece;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(enabled && visible) {
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int i = getHoverState(hovered);

			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F);
			GlStateManager.translate(xPosition, yPosition, 0);
			piece.draw();
			GlStateManager.popMatrix();

			if(i == 2)
				piece.getTooltip(gui.tooltip);
		}
	}

}
