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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.common.Psi;

public class MessageVisualEffect extends NetworkMessage<MessageVisualEffect> {

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
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;
		World world = Minecraft.getMinecraft().world;

		ClientTicker.addAction(() -> {
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

							world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
									x + world.rand.nextFloat() * width * 2.0F - width - d0 * d3,
									y + world.rand.nextFloat() * height - d1 * d3,
									z + world.rand.nextFloat() * width * 2.0F - width - d2 * d3, d0, d1, d2);
						}
					}
					break;
			}
		});

		return null;
	}

}
