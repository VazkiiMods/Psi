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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.client.PsiClientRuntime;

public record MessageAdditiveMotion(int entityID, double motionX, double motionY,
		double motionZ, boolean predictionEligible) implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_additive_motion");
	public static final CustomPacketPayload.Type<MessageAdditiveMotion> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageAdditiveMotion> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, MessageAdditiveMotion::entityID,
			ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionX,
			ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionY,
			ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionZ,
			ByteBufCodecs.BOOL, MessageAdditiveMotion::predictionEligible,
			MessageAdditiveMotion::new);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		Level world = player.level();
		Entity entity = world.getEntity(entityID);
		if(entity != null) {
			Vec3 motion = new Vec3(motionX, motionY, motionZ);
			if(predictionEligible) {
				motion = PsiClientRuntime.reconcilePredictedMotion(motion);
			}
			entity.setDeltaMovement(entity.getDeltaMovement().add(motion));
		}
	}
}
