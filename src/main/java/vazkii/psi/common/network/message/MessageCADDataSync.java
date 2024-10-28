/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

public record MessageCADDataSync(CompoundTag cmp) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_cad_data_sync");
    public static final CustomPacketPayload.Type<MessageCADDataSync> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageCADDataSync> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, MessageCADDataSync::cmp,
            MessageCADDataSync::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ItemStack cad = PsiAPI.getPlayerCAD(Psi.proxy.getClientPlayer());
            if (!cad.isEmpty() && cad.getCapability(PsiAPI.CAD_DATA_CAPABILITY) != null) {
                cad.getCapability(PsiAPI.CAD_DATA_CAPABILITY).deserializeNBT(ctx.player().registryAccess(), cmp);
            }
        });
    }

}
