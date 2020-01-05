/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [02/02/2019, 9:14:30 (EST)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.common.Psi;

public class MessageVisualEffect implements IMessage {

	public static final int TYPE_CRAFT = 0;

	public int color;
	public double x, y, z;
	public double width, height, offset;

	public int effectType;

	public MessageVisualEffect() {
		// NO-OP
	}

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

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;

		context.enqueueWork(() -> {
			World world = Minecraft.getInstance().world;
			switch (effectType) {
				case TYPE_CRAFT:
					for(int i = 0; i < 5; i++) {
						double particleX = x + (Math.random() - 0.5) * 2.1 * width;
						double particleY = y - offset;
						double particleZ = z + (Math.random() - 0.5) * 2.1 * width;
						float grav = -0.05F - (float) Math.random() * 0.01F;
						Psi.proxy.sparkleFX(particleX, particleY, particleZ, r, g, b, grav, 3.5F, 15);

						double m = 0.01;
						double d3 = 10.0D;
						for(int j = 0; j < 3; j++) {
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
