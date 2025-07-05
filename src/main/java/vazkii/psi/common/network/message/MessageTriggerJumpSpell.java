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
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.common.Psi;

public record MessageTriggerJumpSpell() implements CustomPacketPayload {

	public static final ResourceLocation ID = Psi.location("message_trigger_jump_spell");
	public static final CustomPacketPayload.Type<MessageTriggerJumpSpell> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageTriggerJumpSpell> CODEC = new StreamCodec<>() {
        public MessageTriggerJumpSpell decode(RegistryFriendlyByteBuf pBuffer) {
            return new MessageTriggerJumpSpell();
        }

        public void encode(RegistryFriendlyByteBuf pBuffer, MessageTriggerJumpSpell message) {
        }
    };

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> PsiArmorEvent.post(new PsiArmorEvent(ctx.player(), PsiArmorEvent.JUMP)));
	}
}
