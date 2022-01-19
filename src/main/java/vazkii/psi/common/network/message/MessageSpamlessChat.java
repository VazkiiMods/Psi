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
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSpamlessChat {

	private final Component message;
	private static final int BASE_MAGIC = 696969;
	private final int magic;

	public MessageSpamlessChat(Component message, int magic) {
		this.message = message;
		this.magic = BASE_MAGIC + magic;
	}

	public MessageSpamlessChat(FriendlyByteBuf buf) {
		this.message = buf.readComponent();
		this.magic = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeComponent(message);
		buf.writeInt(magic);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ChatComponent chatGui = Minecraft.getInstance().gui.getChat();
			chatGui.removeById(magic);
			chatGui.addMessage(message, magic);
		});
		return true;
	}
}
