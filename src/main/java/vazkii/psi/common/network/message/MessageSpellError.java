/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.client.gui.GuiProgrammer;

import java.util.function.Supplier;

public class MessageSpellError {
	private final String message;
	private final int x;
	private final int y;

	public MessageSpellError(String message, int x, int y) {
		this.message = message;
		this.x = x;
		this.y = y;
	}

	public MessageSpellError(FriendlyByteBuf buf) {
		this.message = buf.readUtf();
		this.x = buf.readInt();
		this.y = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(message);
		buf.writeInt(x);
		buf.writeInt(y);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ChatComponent chatGui = Minecraft.getInstance().gui.getChat();
			Component chatMessage = new TranslatableComponent(message, GuiProgrammer.convertIntToLetter(x), y).setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
			chatGui.addMessage(chatMessage);
		});
		return true;
	}
}
