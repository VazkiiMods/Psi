/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

// https://github.com/Vazkii/Botania/blob/1.15/src/main/java/vazkii/botania/client/fx/FXSparkle.java
@OnlyIn(Dist.CLIENT)
public class FXSparkle extends TextureSheetParticle {

	private static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(@NotNull Tesselator tessellator, @NotNull TextureManager textureManager) {
			return beginRenderCommon(tessellator, textureManager);
		}

		@Override
		public String toString() {
			return "psi:sparkle";
		}
	};

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

		setSize(0.01F, 0.01F);
		// 10 is the sum of the infinite geometric series defined by the drag value of 0.9
		// This is expanding the AABB to contain everywhere the particle will travel
		this.setBoundingBox(this.getBoundingBox().inflate(mx * 10, my * 10, mz * 10));
		xo = x;
		yo = y;
		zo = z;
		setSpriteFromAge(sprite);
	}

	private static BufferBuilder beginRenderCommon(Tesselator tessellator, TextureManager textureManager) {
		Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
		AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
		tex.setFilter(true, false);
		return tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
	}

	@Override
	public float getQuadSize(float partialTicks) {
		return quadSize * (lifetime - age + 1) / (float) lifetime;
	}

	@Override
	public void tick() {
		if(age++ >= lifetime) {
			remove();
			return;
		}

		xo = x;
		yo = y;
		zo = z;

		x += xd;
		y += yd;
		z += zd;

		xd *= 0.9f;
		yd *= 0.9f;
		zd *= 0.9f;

		if(onGround) {
			xd *= 0.7f;
			zd *= 0.7f;
		}
	}

	@NotNull
	@Override
	public ParticleRenderType getRenderType() {
		return NORMAL_RENDER;
	}

	public static class Factory implements ParticleProvider<SparkleParticleData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public TextureSheetParticle createParticle(SparkleParticleData data, @NotNull ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
			return new FXSparkle(world, x, y, z, data.size(), data.r(), data.g(), data.b(), data.m(), mx, my, mz, sprite);
		}
	}
}
