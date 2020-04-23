/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
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
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSpamlessChat {

	private final ITextComponent message;
	private static final int BASE_MAGIC = 696969;
	private final int magic;

	public MessageSpamlessChat(ITextComponent message, int magic) {
		this.message = message;
		this.magic = BASE_MAGIC + magic;
	}

	public MessageSpamlessChat(PacketBuffer buf) {
		this.message = buf.readTextComponent();
		this.magic = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeTextComponent(message);
		buf.writeInt(magic);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			NewChatGui chatGui = Minecraft.getInstance().ingameGUI.getChatGUI();
			chatGui.deleteChatLine(magic);
			chatGui.printChatMessageWithOptionalDeletion(message, magic);
		});
		return true;
	}
}
