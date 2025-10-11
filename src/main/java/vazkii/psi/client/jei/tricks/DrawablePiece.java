/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.tricks;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;

import mezz.jei.api.gui.drawable.IDrawableStatic;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.spell.SpellPiece;

public record DrawablePiece(SpellPiece piece) implements IDrawableStatic {

	@Override
	public void draw(GuiGraphics graphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		graphics.pose().pushPose();
		graphics.pose().translate(xOffset, yOffset, 0);

		MultiBufferSource.BufferSource buffers = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
		piece.drawBackground(graphics.pose(), buffers, 0xF000F0);

		buffers.endBatch();

		graphics.pose().popPose();
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}

	@Override
	public void draw(@NotNull GuiGraphics graphics, int xOff, int yOff) {
		draw(graphics, xOff, yOff, 0, 0, 0, 0);
	}
}
