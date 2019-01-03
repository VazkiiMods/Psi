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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.psi.common.lib.LibResources;

import java.util.ArrayDeque;
import java.util.Queue;

public class FXWisp extends FXQueued {

	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_WISP_LARGE);

	public static Queue<FXWisp> queuedRenders = new ArrayDeque<>();
	public static Queue<FXWisp> queuedDepthIgnoringRenders = new ArrayDeque<>();

	private boolean depthTest;
	private float moteParticleScale;
	private int moteHalfLife;

	public FXWisp(World world, double d, double d1, double d2,  float size, float red, float green, float blue, boolean distanceLimit, boolean depthTest, float maxAgeMul) {
		super(world, d, d1, d2, size, red, green, blue, (int) (28 / (Math.random() * 0.3D + 0.7D) * maxAgeMul));
		moteParticleScale = particleScale / size;
		this.depthTest = depthTest;

		moteHalfLife = particleMaxAge / 2;

		Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();

		if (distanceLimit) {
			int visibleDistance = 50;
			if (!Minecraft.getMinecraft().gameSettings.fancyGraphics)
				visibleDistance = 25;

			if (viewEntity == null || viewEntity.getDistance(posX, posY, posZ) > visibleDistance)
				particleMaxAge = 0;
		}
	}

	@Override
	protected boolean hasSlowdown() {
		return true;
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

	@Override
	protected void incrementQueueCount() {
		if (depthTest)
			ParticleRenderDispatcher.wispFxCount++;
		else
			ParticleRenderDispatcher.depthIgnoringWispFxCount++;
	}

	@Override
	protected float getScale() {
		float agescale = particleAge / (float) moteHalfLife;
		if (agescale > 1F)
			agescale = 2 - agescale;

		particleScale = moteParticleScale * agescale;

		return 0.5F * particleScale;
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

		GlStateManager.disableDepth();
		FXQueued.dispatchQueuedRenders(tessellator, particles, queuedDepthIgnoringRenders);
		GlStateManager.enableDepth();
	}
}
