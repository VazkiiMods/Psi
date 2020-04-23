/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageVisualEffect {

	public static final int TYPE_CRAFT = 0;

	private final int color;
	private final double x, y, z;
	private final double width, height, offset;

	private final int effectType;

	public MessageVisualEffect(int color, double x, double y, double z, double width, double height, double offset, int effectType) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.offset = offset;
		this.effectType = effectType;
	}

	public MessageVisualEffect(PacketBuffer buf) {
		this.color = buf.readInt();
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.width = buf.readDouble();
		this.height = buf.readDouble();
		this.offset = buf.readDouble();
		this.effectType = buf.readVarInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(color);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(width);
		buf.writeDouble(height);
		buf.writeDouble(offset);
		buf.writeVarInt(effectType);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;

		context.get().enqueueWork(() -> {
			World world = Psi.proxy.getClientWorld();
			switch (effectType) {
			case TYPE_CRAFT:
				for (int i = 0; i < 5; i++) {
					double particleX = x + (Math.random() - 0.5) * 2.1 * width;
					double particleY = y - offset;
					double particleZ = z + (Math.random() - 0.5) * 2.1 * width;
					float grav = -0.05F - (float) Math.random() * 0.01F;
					Psi.proxy.sparkleFX(particleX, particleY, particleZ, r, g, b, grav, 3.5F, 15);

					double m = 0.01;
					double d3 = 10.0D;
					for (int j = 0; j < 3; j++) {
						double d0 = world.rand.nextGaussian() * m;
						double d1 = world.rand.nextGaussian() * m;
						double d2 = world.rand.nextGaussian() * m;

						world.addParticle(ParticleTypes.EXPLOSION,
								x + world.rand.nextFloat() * width * 2.0F - width - d0 * d3,
								y + world.rand.nextFloat() * height - d1 * d3,
								z + world.rand.nextFloat() * width * 2.0F - width - d2 * d3, d0, d1, d2);
					}
				}
				break;
			}
		});

		return true;
	}

}
