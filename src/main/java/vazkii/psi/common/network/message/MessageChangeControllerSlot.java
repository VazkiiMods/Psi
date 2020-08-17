/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
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

import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.common.core.handler.PlayerDataHandler;

import java.util.function.Supplier;

public class MessageChangeControllerSlot {

	private final int controlSlot;
	private final int slot;

	public MessageChangeControllerSlot(int controlSlot, int slot) {
		this.controlSlot = controlSlot;
		this.slot = slot;
	}

	public MessageChangeControllerSlot(PacketBuffer buf) {
		this.controlSlot = buf.readVarInt();
		this.slot = buf.readVarInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(controlSlot);
		buf.writeVarInt(slot);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			ServerPlayerEntity player = context.get().getSender();
			ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
			if (!stack.isEmpty() && stack.getItem() instanceof ISocketableController) {
				((ISocketableController) stack.getItem()).setSelectedSlot(player, stack, controlSlot, slot);
			} else {
				stack = player.getHeldItem(Hand.OFF_HAND);
				if (!stack.isEmpty() && stack.getItem() instanceof ISocketableController) {
					((ISocketableController) stack.getItem()).setSelectedSlot(player, stack, controlSlot, slot);
				}
			}
			PlayerDataHandler.get(player).stopLoopcast();
		});

		return true;
	}

}
