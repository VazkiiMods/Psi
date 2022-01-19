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
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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

	public MessageSpellError(PacketBuffer buf) {
		this.message = buf.readUtf();
		this.x = buf.readInt();
		this.y = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeUtf(message);
		buf.writeInt(x);
		buf.writeInt(y);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			NewChatGui chatGui = Minecraft.getInstance().gui.getChat();
			ITextComponent chatMessage = new TranslationTextComponent(message, GuiProgrammer.convertIntToLetter(x), y).setStyle(Style.EMPTY.withColor(TextFormatting.RED));
			chatGui.addMessage(chatMessage);
		});
		return true;
	}
}
