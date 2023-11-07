/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.function.Supplier;

public class MessageChangeSocketableSlot {

	private final int slot;

	public MessageChangeSocketableSlot(int slot) {
		this.slot = slot;
	}

	public MessageChangeSocketableSlot(FriendlyByteBuf buf) {
		this.slot = buf.readVarInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(slot);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayer player = context.get().getSender();
			ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

			if(!stack.isEmpty() && stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).isPresent()) {
				stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(cap -> cap.setSelectedSlot(slot));
			} else {
				stack = player.getItemInHand(InteractionHand.OFF_HAND);
				if(!stack.isEmpty()) {
					stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(cap -> cap.setSelectedSlot(slot));
				}
			}
			PlayerDataHandler.get(player).stopLoopcast();
		});

		return true;
	}

}
