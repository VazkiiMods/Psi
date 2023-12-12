/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSpellPiece extends Button {
	public SpellPiece piece;
	final GuiProgrammer gui;

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y, Button.OnPress pressable) {
		super(x, y, 16, 16, Component.empty(), pressable, DEFAULT_NARRATION);
		this.gui = gui;
		this.piece = piece;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float pTicks) {
		if(active && visible) {
			boolean hover = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;

			MultiBufferSource.BufferSource buffers = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			graphics.pose().pushPose();
			graphics.pose().translate(getX(), getY(), 0);
			piece.draw(graphics, buffers, 0xF000F0);
			buffers.endBatch();
			graphics.pose().popPose();

			if(hover) {
				piece.getTooltip(gui.tooltip);
				graphics.blit(GuiProgrammer.texture, getX(), getY(), 16, gui.ySize, 16, 16);
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
