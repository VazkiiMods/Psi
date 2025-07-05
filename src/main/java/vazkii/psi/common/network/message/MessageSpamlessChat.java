/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import vazkii.psi.common.Psi;

import java.nio.ByteBuffer;
import java.util.ListIterator;

public class MessageSpamlessChat implements CustomPacketPayload {

	public static final ResourceLocation ID = Psi.location("message_spamless_chat");
	public static final CustomPacketPayload.Type<MessageSpamlessChat> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSpamlessChat> CODEC = new StreamCodec<>() {
        public MessageSpamlessChat decode(RegistryFriendlyByteBuf pBuffer) {
            return new MessageSpamlessChat(ComponentSerialization.TRUSTED_STREAM_CODEC.decode(pBuffer), pBuffer.readInt());
        }

        public void encode(RegistryFriendlyByteBuf pBuffer, MessageSpamlessChat message) {
            ComponentSerialization.TRUSTED_STREAM_CODEC.encode(pBuffer, message.message);
            pBuffer.writeInt(message.magic);
        }
    };
	private static final int BASE_MAGIC = 696969;
	private final Component message;
	private final MessageSignature signature;
	private final int magic;

	public MessageSpamlessChat(Component message, int magic) {
		this.message = message;
		this.magic = BASE_MAGIC + magic;
		this.signature = new MessageSignature(ByteBuffer.allocate(256).putInt(this.magic).array());
	}

	public static void deleteMessage(ChatComponent chatGui, MessageSignature pMessageSignature) {
		ListIterator<GuiMessage> listiterator = chatGui.allMessages.listIterator();

		while(listiterator.hasNext()) {
			GuiMessage guimessage = listiterator.next();
			if(pMessageSignature.equals(guimessage.signature())) {
				listiterator.remove();
				break;
			}
		}
		chatGui.refreshTrimmedMessages();
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			ChatComponent chatGui = Minecraft.getInstance().gui.getChat();
			MessageSpamlessChat.deleteMessage(chatGui, signature);
			chatGui.addMessage(message, signature, GuiMessageTag.system());
		});
	}
}
