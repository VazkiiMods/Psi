/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [14/01/2016, 22:48:01 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageChangeSocketableSlot implements IMessage {

	public int slot;

	public MessageChangeSocketableSlot() { }

	public MessageChangeSocketableSlot(int slot) {
		this.slot = slot;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ServerPlayerEntity player = context.getSender();
			ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

			if(!stack.isEmpty() && ISocketableCapability.isSocketable(stack))
				ISocketableCapability.socketable(stack).setSelectedSlot(slot);
			else {
				stack = player.getHeldItem(Hand.OFF_HAND);
				if(!stack.isEmpty() && ISocketableCapability.isSocketable(stack))
					ISocketableCapability.socketable(stack).setSelectedSlot(slot);
			}
			PlayerDataHandler.get(player).stopLoopcast();
		});

		return true;
	}

}
