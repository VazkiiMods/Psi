/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.message.*;

public class MessageRegister {
    public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3 = new StreamCodec<RegistryFriendlyByteBuf, Vec3>() {
        public Vec3 decode(RegistryFriendlyByteBuf pBuffer) {
            return pBuffer.readVec3();
        }

        public void encode(RegistryFriendlyByteBuf pBuffer, Vec3 pVec3) {
            pBuffer.writeVec3(pVec3);
        }
    };
    private static final String VERSION = "3";

    @SubscribeEvent
    public static void onRegisterPayloadHandler(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(LibMisc.MOD_ID)
                .versioned(VERSION)
                .optional();
        registrar.playBidirectional(MessageAdditiveMotion.TYPE, MessageAdditiveMotion.CODEC, MessageAdditiveMotion::handle);
        registrar.playBidirectional(MessageBlink.TYPE, MessageBlink.CODEC, MessageBlink::handle);
        registrar.playBidirectional(MessageChangeControllerSlot.TYPE, MessageChangeControllerSlot.CODEC, MessageChangeControllerSlot::handle);
        registrar.playBidirectional(MessageChangeSocketableSlot.TYPE, MessageChangeSocketableSlot.CODEC, MessageChangeSocketableSlot::handle);
        registrar.playBidirectional(MessageDataSync.TYPE, MessageDataSync.CODEC, MessageDataSync::handle);
        registrar.playBidirectional(MessageDeductPsi.TYPE, MessageDeductPsi.CODEC, MessageDeductPsi::handle);
        registrar.playBidirectional(MessageEidosSync.TYPE, MessageEidosSync.CODEC, MessageEidosSync::handle);
        registrar.playBidirectional(MessageLoopcastSync.TYPE, MessageLoopcastSync.CODEC, MessageLoopcastSync::handle);
        registrar.playBidirectional(MessageParticleTrail.TYPE, MessageParticleTrail.CODEC, MessageParticleTrail::handle);
        registrar.playBidirectional(MessageSpamlessChat.TYPE, MessageSpamlessChat.CODEC, MessageSpamlessChat::handle);
        registrar.playBidirectional(MessageSpellError.TYPE, MessageSpellError.CODEC, MessageSpellError::handle);
        registrar.playBidirectional(MessageSpellModified.TYPE, MessageSpellModified.CODEC, MessageSpellModified::handle);
        registrar.playBidirectional(MessageTriggerJumpSpell.TYPE, MessageTriggerJumpSpell.CODEC, MessageTriggerJumpSpell::handle);
        registrar.playBidirectional(MessageVisualEffect.TYPE, MessageVisualEffect.CODEC, MessageVisualEffect::handle);

    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, MSG message) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersTrackingEntity(Entity entity, MSG message) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersTrackingEntityAndSelf(Entity entity, MSG message) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersInDimension(ServerLevel level, MSG message) {
        PacketDistributor.sendToPlayersInDimension(level, message);
    }
}
