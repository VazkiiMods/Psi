/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.client.PsiClientRuntime;

public class MessageSpamlessChat implements CustomPacketPayload {

	public static final ResourceLocation ID = PsiAPI.location("message_spamless_chat");
	public static final CustomPacketPayload.Type<MessageSpamlessChat> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSpamlessChat> CODEC = new StreamCodec<>() {
		public @NotNull MessageSpamlessChat decode(@NotNull RegistryFriendlyByteBuf pBuffer) {
			return new MessageSpamlessChat(ComponentSerialization.TRUSTED_STREAM_CODEC.decode(pBuffer), pBuffer.readInt());
		}

		public void encode(@NotNull RegistryFriendlyByteBuf pBuffer, MessageSpamlessChat message) {
			ComponentSerialization.TRUSTED_STREAM_CODEC.encode(pBuffer, message.message);
			pBuffer.writeInt(message.magic);
		}
	};
	private static final int BASE_MAGIC = 696969;
	private final Component message;
	private final int magic;

	public MessageSpamlessChat(Component message, int magic) {
		this.message = message;
		this.magic = BASE_MAGIC + magic;
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(Player player) {
		PsiClientRuntime.showSpamlessChat(message, magic);
	}
}
