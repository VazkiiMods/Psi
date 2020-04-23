/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.function.Supplier;

public class MessageChangeSocketableSlot {

	private final int slot;

	public MessageChangeSocketableSlot(int slot) {
		this.slot = slot;
	}

	public MessageChangeSocketableSlot(PacketBuffer buf) {
		this.slot = buf.readVarInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(slot);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();
			ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

			if (!stack.isEmpty()) {
				stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(cap -> cap.setSelectedSlot(slot));
			} else {
				stack = player.getHeldItem(Hand.OFF_HAND);
				if (!stack.isEmpty()) {
					stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).ifPresent(cap -> cap.setSelectedSlot(slot));
				}
			}
			PlayerDataHandler.get(player).stopLoopcast();
		});

		return true;
	}

}
