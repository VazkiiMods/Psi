/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/FXSparkle.java
public class FXSparkle extends SpriteTexturedParticle {

	public int multipler;
	public final int particle = 16;
	private final IAnimatedSprite sprite;

	public FXSparkle(World world, double x, double y, double z, float size,
			float red, float green, float blue, int m, double mx, double my, double mz, IAnimatedSprite sprite) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 0.5F;
		particleGravity = 0;
		motionX = mx;
		motionY = my;
		motionZ = mz;
		particleScale *= size;
		maxAge = 3 * m;
		multipler = m;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		this.sprite = sprite;
		selectSpriteWithAge(sprite);
	}

	@Override
	public float getScale(float partialTicks) {
		return particleScale * (maxAge - age + 1) / (float) maxAge;
	}

	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (age++ >= maxAge) {
			setExpired();
		}
//		if (!noClip)
//			pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		motionX *= 0.9f;
		motionY *= 0.9f;
		motionZ *= 0.9f;

		if (onGround) {
			motionX *= 0.7f;
			motionZ *= 0.7f;
		}
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return NORMAL_RENDER;
	}

	private static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		RenderSystem.disableLighting();
		textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
		Texture tex = textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
		tex.setBlurMipmapDirect(true, false);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	private static void endRenderCommon() {
		Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}

	private static final IParticleRenderType NORMAL_RENDER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "psi:sparkle";
		}
	};

}
