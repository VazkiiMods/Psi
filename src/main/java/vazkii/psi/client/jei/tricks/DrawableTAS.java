/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.jei.tricks;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawableStatic;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * Like JEI's DrawableSprite, but works for any {@link TextureAtlasSprite}.
 */
public class DrawableTAS implements IDrawableStatic {
	private final TextureAtlasSprite sprite;

	public DrawableTAS(TextureAtlasSprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void draw(int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		int textureWidth = sprite.getWidth();
		int textureHeight = sprite.getHeight();
		int x = xOffset + maskLeft;
		int y = yOffset + maskTop;
		int width = textureWidth - maskRight - maskLeft;
		int height = textureHeight - maskBottom - maskTop;
		float uSize = sprite.getMaxU() - sprite.getMinU();
		float vSize = sprite.getMaxV() - sprite.getMinV();
		float minU = sprite.getMinU() + uSize * ((float) maskLeft / (float) textureWidth);
		float minV = sprite.getMinV() + vSize * ((float) maskTop / (float) textureHeight);
		float maxU = sprite.getMaxU() - uSize * ((float) maskRight / (float) textureWidth);
		float maxV = sprite.getMaxV() - vSize * ((float) maskBottom / (float) textureHeight);

		RenderSystem.bindTexture(sprite.getAtlas().getGlTextureId());
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.vertex(x, y + height, 0.0D).texture(minU, maxV).endVertex();
		buf.vertex(x + width, y + height, 0.0D).texture(maxU, maxV).endVertex();
		buf.vertex(x + width, y, 0.0D).texture(maxU, minV).endVertex();
		buf.vertex(x, y, 0.0D).texture(minU, minV).endVertex();
		tessellator.draw();
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
	public void draw(int xOff, int yOff) {
		draw(xOff, yOff, 0, 0, 0, 0);
	}
}
