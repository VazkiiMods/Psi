/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.tricks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawableStatic;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

/**
 * Like JEI's DrawableSprite, but works for any {@link TextureAtlasSprite}.
 */
public class DrawableTAS implements IDrawableStatic {
	private final TextureAtlasSprite sprite;

	public DrawableTAS(TextureAtlasSprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void draw(MatrixStack ms, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		int textureWidth = sprite.getWidth();
		int textureHeight = sprite.getHeight();
		int x = xOffset + maskLeft;
		int y = yOffset + maskTop;
		int width = textureWidth - maskRight - maskLeft;
		int height = textureHeight - maskBottom - maskTop;
		float uSize = sprite.getU1() - sprite.getU0();
		float vSize = sprite.getV1() - sprite.getV0();
		float minU = sprite.getU0() + uSize * ((float) maskLeft / (float) textureWidth);
		float minV = sprite.getV0() + vSize * ((float) maskTop / (float) textureHeight);
		float maxU = sprite.getU1() - uSize * ((float) maskRight / (float) textureWidth);
		float maxV = sprite.getV1() - vSize * ((float) maskBottom / (float) textureHeight);

		Matrix4f matrix = ms.last().pose();
		RenderSystem.bindTexture(sprite.atlas().getId());
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuilder();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.vertex(matrix, x, y + height, 0.0f).uv(minU, maxV).endVertex();
		buf.vertex(matrix, x + width, y + height, 0.0f).uv(maxU, maxV).endVertex();
		buf.vertex(matrix, x + width, y, 0.0f).uv(maxU, minV).endVertex();
		buf.vertex(matrix, x, y, 0.0f).uv(minU, minV).endVertex();
		tessellator.end();
	}

	@Override
	public int getWidth() {
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		return sprite.getHeight();
	}

	@Override
	public void draw(MatrixStack ms, int xOff, int yOff) {
		draw(ms, xOff, yOff, 0, 0, 0, 0);
	}
}
