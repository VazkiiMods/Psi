/**
 * This class was created by <Azanor>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.psi.client.fx;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.psi.common.lib.LibResources;

import java.util.ArrayDeque;
import java.util.Queue;

public class FXWisp extends FXQueued {

	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_WISP_LARGE);

	public static final Queue<FXWisp> queuedRenders = new ArrayDeque<>();
	public static final Queue<FXWisp> queuedDepthIgnoringRenders = new ArrayDeque<>();

	private final boolean depthTest;
	private final float moteParticleScale;
	private final int moteHalfLife;

	public FXWisp(World world, double d, double d1, double d2,  float size, float red, float green, float blue, boolean distanceLimit, boolean depthTest, float maxAgeMul) {
		super(world, d, d1, d2, size, red, green, blue, (int) (28 / (Math.random() * 0.3D + 0.7D) * maxAgeMul));

		this.depthTest = depthTest;

		moteHalfLife = maxAge / 2;
		//I am very unsure of this
		moteParticleScale = getScale();

		Entity viewEntity = Minecraft.getInstance().getRenderViewEntity();

		if (distanceLimit) {
			int visibleDistance = 50;
			if (!Minecraft.getInstance().gameSettings.fancyGraphics)
				visibleDistance = 25;

			if (viewEntity == null || MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ) > visibleDistance)
				maxAge = 0;
		}
	}

	@Override
	protected boolean hasSlowdown() {
		return true;
	}

	@Override
	protected double slowdownFactor() {
		return 0.98;
	}

	@Override
	protected boolean hasFriction() {
		return false;
	}

	@Override
	protected void addToQueue() {
		if (depthTest)
			queuedRenders.add(this);
		else
			queuedDepthIgnoringRenders.add(this);
	}

	public int getMoteHalfLife() {
		return moteHalfLife;
	}

	@Override
	protected void incrementQueueCount() {
		if (depthTest)
			ParticleRenderDispatcher.wispFxCount++;
		else
			ParticleRenderDispatcher.depthIgnoringWispFxCount++;
	}

	@Override
	protected float getScale() {
		float agescale = age / (float) moteHalfLife;
		if (agescale > 1F)
			agescale = 2 - agescale;

		setSize(moteParticleScale * agescale, moteParticleScale * agescale);

		return 0.5F * moteParticleScale * agescale;
	}

	@Override
	protected float getMinU() {
		return 0;
	}

	@Override
	protected float getMaxU() {
		return 1;
	}

	@Override
	protected float getMinV() {
		return 0;
	}

	@Override
	protected float getMaxV() {
		return 1;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		ParticleRenderDispatcher.wispFxCount = 0;
		ParticleRenderDispatcher.depthIgnoringWispFxCount = 0;

		FXQueued.dispatchQueuedRenders(tessellator, particles, queuedRenders);

		GlStateManager.disableDepthTest();
		FXQueued.dispatchQueuedRenders(tessellator, particles, queuedDepthIgnoringRenders);
		GlStateManager.enableDepthTest();
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.CUSTOM;
	}
}
