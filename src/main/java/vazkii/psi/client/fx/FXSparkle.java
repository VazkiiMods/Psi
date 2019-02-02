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

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.psi.common.lib.LibResources;

import java.util.ArrayDeque;
import java.util.Queue;

public class FXSparkle extends FXQueued {

	public static final ResourceLocation particles = new ResourceLocation(LibResources.MISC_PARTICLES);

	public static final Queue<FXSparkle> queuedRenders = new ArrayDeque<>();

	public final boolean shrink = true;
	public final int particle = 16;
	public final int multiplier;

	public FXSparkle(World world, double x, double y, double z, float size, float red, float green, float blue, int m) {
		super(world, x, y, z, size, red, green, blue, m * 3);
		multiplier = m;
	}
	
	public void setSpeed(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		ParticleRenderDispatcher.sparkleFxCount = 0;
		FXQueued.dispatchQueuedRenders(tessellator, particles, queuedRenders);
	}

	@Override
	protected float getScale() {
		float rotation = 0.1F * particleScale;
		if (shrink)
			rotation *= (particleMaxAge - particleAge + 1f) / particleMaxAge;
		return rotation;
	}

	@Override
	protected float getMinU() {
		int part = particle + particleAge / multiplier;

		return (part % 8) / 8f;
	}

	@Override
	protected float getMaxU() {
		return getMinU() + 1 / 8f;
	}

	@Override
	protected float getMinV() {
		int part = particle + particleAge / multiplier;
		return (part >> 3) / 8f;
	}

	@Override
	protected float getMaxV() {
		return getMinV() + 1 / 8f;
	}

	@Override
	protected void addToQueue() {
		queuedRenders.add(this);
	}

	@Override
	protected void incrementQueueCount() {
		ParticleRenderDispatcher.sparkleFxCount++;
	}

	@Override
	protected boolean hasSlowdown() {
		return true;
	}

	@Override
	protected boolean hasFriction() {
		return true;
	}
}
