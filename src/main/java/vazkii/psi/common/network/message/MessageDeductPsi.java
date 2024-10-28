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
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.lib.LibMisc;

public record MessageDeductPsi(int prev, int current, int cd, boolean shatter) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_deduct_psi");
    public static final CustomPacketPayload.Type<MessageDeductPsi> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageDeductPsi> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MessageDeductPsi::prev,
            ByteBufCodecs.INT, MessageDeductPsi::current,
            ByteBufCodecs.INT, MessageDeductPsi::cd,
            ByteBufCodecs.BOOL, MessageDeductPsi::shatter,
            MessageDeductPsi::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = Psi.proxy.getClientPlayer();
            if (player != null) {
                PlayerData data = PlayerDataHandler.get(player);
                data.lastAvailablePsi = data.availablePsi;
                data.availablePsi = current;
                data.regenCooldown = cd;
                data.deductTick = true;
                data.addDeduction(prev, prev - current, shatter);
            }
        });
    }

}
