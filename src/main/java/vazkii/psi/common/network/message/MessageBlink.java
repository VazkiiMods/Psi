/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.Psi;

/**
 * This is needed instead of a serverside position set to avoid jittering, especially under lag.
 */
public record MessageBlink(double offX, double offY, double offZ, boolean predictionEligible) implements CustomPacketPayload {

	public static final ResourceLocation ID = Psi.location("message_blink");
	public static final CustomPacketPayload.Type<MessageBlink> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageBlink> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, MessageBlink::offX,
			ByteBufCodecs.DOUBLE, MessageBlink::offY,
			ByteBufCodecs.DOUBLE, MessageBlink::offZ,
			ByteBufCodecs.BOOL, MessageBlink::predictionEligible,
			MessageBlink::new);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			Entity entity = Psi.proxy.getClientPlayer();
			if(entity != null) {
				Vec3 offset = new Vec3(offX, offY, offZ);
				if(predictionEligible) {
					offset = Psi.proxy.reconcilePredictedBlink(offset);
				}
				entity.setPos(entity.position().add(offset));
			}
		});
	}
}
