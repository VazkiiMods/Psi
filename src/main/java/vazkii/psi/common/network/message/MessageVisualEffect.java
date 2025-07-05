/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.common.Psi;

public record MessageVisualEffect(int color, double x, double y, double z, double width, double height, double offset,
		int effectType) implements CustomPacketPayload {

	public static final int TYPE_CRAFT = 0;

	public static final ResourceLocation ID = Psi.location("message_visual_effect");
	public static final CustomPacketPayload.Type<MessageVisualEffect> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageVisualEffect> CODEC = new StreamCodec<>() {
		public MessageVisualEffect decode(RegistryFriendlyByteBuf pBuffer) {
			return new MessageVisualEffect(
					pBuffer.readInt(),
					pBuffer.readDouble(),
					pBuffer.readDouble(),
					pBuffer.readDouble(),
					pBuffer.readDouble(),
					pBuffer.readDouble(),
					pBuffer.readDouble(),
					pBuffer.readInt()
			);
		}

		public void encode(RegistryFriendlyByteBuf pBuffer, MessageVisualEffect message) {
			pBuffer.writeInt(message.color());
			pBuffer.writeDouble(message.x());
			pBuffer.writeDouble(message.y());
			pBuffer.writeDouble(message.z());
			pBuffer.writeDouble(message.width());
			pBuffer.writeDouble(message.height());
			pBuffer.writeDouble(message.offset());
			pBuffer.writeInt(message.effectType());
		}
	};

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;

		ctx.enqueueWork(() -> {
			Level world = Psi.proxy.getClientWorld();
			switch(effectType) {
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
						double d0 = world.random.nextGaussian() * m;
						double d1 = world.random.nextGaussian() * m;
						double d2 = world.random.nextGaussian() * m;

						world.addParticle(ParticleTypes.EXPLOSION,
								x + world.random.nextFloat() * width * 2.0F - width - d0 * d3,
								y + world.random.nextFloat() * height - d1 * d3,
								z + world.random.nextFloat() * width * 2.0F - width - d2 * d3, d0, d1, d2);
					}
				}
				break;
			}
		});
	}
}
