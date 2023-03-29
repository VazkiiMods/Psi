/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.network.chat.Component;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.client.gui.GuiProgrammer;

public class GuiButtonSpellPiece extends Button {
	public SpellPiece piece;
	final GuiProgrammer gui;

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y) {
		super(x, y, 16, 16, Component.empty(), button -> {});
		this.gui = gui;
		this.piece = piece;
	}

	public GuiButtonSpellPiece(GuiProgrammer gui, SpellPiece piece, int x, int y, Button.OnPress pressable) {
		super(x, y, 16, 16, Component.empty(), pressable);
		this.gui = gui;
		this.piece = piece;
	}

	@Override
	public void renderButton(PoseStack ms, int mouseX, int mouseY, float pTicks) {
		if (active && visible) {
			boolean hover = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

			MultiBufferSource.BufferSource buffers = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			ms.pushPose();
			ms.translate(x, y, 0);
			piece.draw(ms, buffers, 0xF000F0);
			buffers.endBatch();
			ms.popPose();
			RenderSystem.setShaderTexture(0, GuiProgrammer.texture);
			if (hover) {
				piece.getTooltip(gui.tooltip);
				blit(ms, x, y, 16, gui.ySize, 16, 16);
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
