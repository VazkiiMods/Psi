/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [03/01/2019, 17:42:02 (GMT)]
 */
package vazkii.psi.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Queue;

public abstract class FXQueued extends Particle {

	// Queue values
	private float partialTicks;
	private float rotationX;
	private float rotationZ;
	private float rotationYZ;
	private float rotationXY;
	private float rotationXZ;

	protected static void dispatchQueuedRenders(Tessellator tessellator, ResourceLocation particles, Queue<? extends FXQueued> queuedRenders) {
		if (!queuedRenders.isEmpty()) {
			GlStateManager.color4f(1, 1, 1, 0.75f);
			Minecraft.getInstance().textureManager.bindTexture(particles);

			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			for (FXQueued sparkle : queuedRenders)
				sparkle.renderQueued(tessellator);
			tessellator.draw();

			queuedRenders.clear();
		}
	}

	public FXQueued(World world, double x, double y, double z, float size, float red, float green, float blue, int maxAge) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);

		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale *= size;
		particleMaxAge = maxAge;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}
	
	public void setSpeed(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}


	protected double slowdownFactor() {
		return 0.9f;
	}

	protected double frictionFactor() {
		return 0.7f;
	}

	protected abstract boolean hasSlowdown();
	protected abstract boolean hasFriction();
	protected abstract void addToQueue();
	protected abstract void incrementQueueCount();
	protected abstract float getScale();
	protected abstract float getMinU();
	protected abstract float getMaxU();
	protected abstract float getMinV();
	protected abstract float getMaxV();

	private void renderQueued(Tessellator tessellator) {
		incrementQueueCount();

		float minU = getMinU();
		float maxU = getMaxU();
		float minV = getMinV();
		float maxV = getMaxV();
		float scale = getScale();
		float x = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
		float y = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
		float z = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

		BufferBuilder buf = tessellator.getBuffer();

		buf.pos(x - rotationX * scale - rotationXY * scale,
				y - rotationZ * scale,
				z - rotationYZ * scale - rotationXZ * scale)
				.tex(maxU, maxV).color(particleRed, particleGreen, particleBlue, 1).endVertex();
		buf.pos(x - rotationX * scale + rotationXY * scale,
				y + rotationZ * scale,
				z - rotationYZ * scale + rotationXZ * scale)
				.tex(maxU, minV).color(particleRed, particleGreen, particleBlue, 1).endVertex();
		buf.pos(x + rotationX * scale + rotationXY * scale,
				y + rotationZ * scale,
				z + rotationYZ * scale + rotationXZ * scale)
				.tex(minU, minV).color(particleRed, particleGreen, particleBlue, 1).endVertex();
		buf.pos(x + rotationX * scale - rotationXY * scale,
				y - rotationZ * scale,
				z + rotationYZ * scale - rotationXZ * scale)
				.tex(minU, maxV).color(particleRed, particleGreen, particleBlue, 1).endVertex();
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity viewEntity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		this.partialTicks = partialTicks;
		this.rotationX = rotationX;
		this.rotationZ = rotationZ;
		this.rotationYZ = rotationYZ;
		this.rotationXY = rotationXY;
		this.rotationXZ = rotationXZ;

		addToQueue();
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge)
			setExpired();

		motionY -= 0.04 * particleGravity;

//		if (!noClip)
//			pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		if (hasSlowdown()) {
			double slowdown = slowdownFactor();
			motionX *= slowdown;
			motionY *= slowdown;
			motionZ *= slowdown;

			if (hasFriction() && onGround) {
				double friction = frictionFactor();
				motionX *= friction;
				motionZ *= friction;
			}
		}
	}

	public void setGravity(float value) {
		particleGravity = value;
	}
}
