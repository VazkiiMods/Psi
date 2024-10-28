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
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

public record MessageAdditiveMotion(int entityID, double motionX, double motionY,
                                    double motionZ) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_additive_motion");
    public static final CustomPacketPayload.Type<MessageAdditiveMotion> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageAdditiveMotion> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MessageAdditiveMotion::entityID,
            ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionX,
            ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionY,
            ByteBufCodecs.DOUBLE, MessageAdditiveMotion::motionZ,
            MessageAdditiveMotion::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level world = Psi.proxy.getClientWorld();
            if (world != null) {
                Entity entity = world.getEntity(entityID);
                if (entity != null) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
                }
            }
        });
    }
}
