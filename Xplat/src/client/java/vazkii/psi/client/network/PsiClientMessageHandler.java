/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.network;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.Style;

import vazkii.psi.common.platform.PsiConfig;
import vazkii.psi.mixin.client.AccessorChatComponent;

import java.nio.ByteBuffer;
import java.util.ListIterator;

public final class PsiClientMessageHandler {

	private PsiClientMessageHandler() {}

	public static void showSpamlessChat(Component message, int magic) {
		MessageSignature signature = new MessageSignature(ByteBuffer.allocate(256).putInt(magic).array());
		ChatComponent chat = Minecraft.getInstance().gui.getChat();
		deleteMessage(chat, signature);
		chat.addMessage(message, signature, GuiMessageTag.system());
	}

	public static void showSpellError(String message, int x, int y) {
		String displayX = PsiConfig.client().letterNumberGridCoordinates()
				? String.valueOf((char) ((x % 27) + 64))
				: String.valueOf(x);
		Component chatMessage = Component.translatable(message, displayX, y)
				.setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
		Minecraft.getInstance().gui.getChat().addMessage(chatMessage);
	}

	private static void deleteMessage(ChatComponent chat, MessageSignature signature) {
		AccessorChatComponent accessor = (AccessorChatComponent) chat;
		ListIterator<GuiMessage> iterator = accessor.psi$getAllMessages().listIterator();
		while(iterator.hasNext()) {
			if(signature.equals(iterator.next().signature())) {
				iterator.remove();
				break;
			}
		}
		accessor.psi$refreshTrimmedMessages();
	}

}
