/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

/**
 * This is needed instead of a serverside position set to avoid jittering, especially under lag.
 */
public record MessageBlink(double offX, double offY, double offZ) implements CustomPacketPayload {

	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_blink");
	public static final CustomPacketPayload.Type<MessageBlink> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageBlink> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, MessageBlink::offX,
			ByteBufCodecs.DOUBLE, MessageBlink::offY,
			ByteBufCodecs.DOUBLE, MessageBlink::offZ,
			MessageBlink::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Entity entity = Psi.proxy.getClientPlayer();
			if(entity != null) {
				entity.setPos(entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ);
			}
		});
	}
}
