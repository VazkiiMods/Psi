/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.lib.LibMisc;

public record MessageSpellError(String message, int x, int y) implements CustomPacketPayload {

	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "message_spell_error");
	public static final CustomPacketPayload.Type<MessageSpellError> TYPE = new Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSpellError> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, MessageSpellError::message,
			ByteBufCodecs.INT, MessageSpellError::x,
			ByteBufCodecs.INT, MessageSpellError::y,
			MessageSpellError::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			ChatComponent chatGui = Minecraft.getInstance().gui.getChat();
			Component chatMessage = Component.translatable(message, GuiProgrammer.convertIntToLetter(x), y).setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
			chatGui.addMessage(chatMessage);
		});
	}
}
