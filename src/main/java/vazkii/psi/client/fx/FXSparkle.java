/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;

import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/FXSparkle.java
public class FXSparkle extends TextureSheetParticle {

	public int multipler;
	public final int particle = 16;
	private final SpriteSet sprite;

	public FXSparkle(ClientLevel world, double x, double y, double z, float size,
			float red, float green, float blue, int m, double mx, double my, double mz, SpriteSet sprite) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		rCol = red;
		gCol = green;
		bCol = blue;
		alpha = 0.5F;
		gravity = 0;
		xd = mx;
		yd = my;
		zd = mz;
		quadSize *= size;
		lifetime = 3 * m;
		multipler = m;
		setSize(0.01F, 0.01F);
		// 10 is the sum of the infinite geometric series defined by the drag value of 0.9
		// This is expanding the AABB to contain everywhere the particle will travel
		this.setBoundingBox(this.getBoundingBox().inflate(mx * 10, my * 10, mz * 10));
		xo = x;
		yo = y;
		zo = z;
		this.sprite = sprite;
		setSpriteFromAge(sprite);
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return quadSize * (lifetime - age + 1) / (float) lifetime;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;

		if (age++ >= lifetime) {
			remove();
		}
//		if (!noClip)
//			pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);

		x += xd;
		y += yd;
		z += zd;

		xd *= 0.9f;
		yd *= 0.9f;
		zd *= 0.9f;

		if (onGround) {
			xd *= 0.7f;
			zd *= 0.7f;
		}
	}

	@Nonnull
	@Override
	public ParticleRenderType getRenderType() {
		return NORMAL_RENDER;
	}

	private static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		//RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		//RenderSystem.disableLighting();
		textureManager.bindForSetup(TextureAtlas.LOCATION_PARTICLES);
		AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
		tex.setFilter(true, false);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
	}

	private static void endRenderCommon() {
		Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
		//RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}

	private static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {
		@Override
		public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
		}

		@Override
		public void end(Tesselator tessellator) {
			tessellator.end();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "psi:sparkle";
		}
	};

}
