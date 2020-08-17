/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;

import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSpellPiece extends Button {
	public SpellPiece piece;
	final GuiProgrammer gui;

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y) {
		super(x, y, 16, 16, "", button -> {});
		this.gui = gui;
		this.piece = piece;
	}

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y, Button.IPressable pressable) {
		super(x, y, 16, 16, "", pressable);
		this.gui = gui;
		this.piece = piece;
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float pTicks) {
		if (active && visible) {
			boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

			IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuffer());
			MatrixStack ms = new MatrixStack();
			ms.translate(x, y, 0);
			piece.draw(ms, buffers, 0xF000F0);
			buffers.draw();

			Minecraft.getInstance().getTextureManager().bindTexture(GuiProgrammer.texture);
			if (hover) {
				piece.getTooltip(gui.tooltip);
				blit(x, y, 16, gui.ySize, 16, 16);
			}

		}
	}

	public SpellPiece getPiece() {
		return piece;
	}

	public String getPieceSortingName() {
		return piece.getSortingName();
	}

	public void setPiece(SpellPiece piece) {
		this.piece = piece;
	}
}
