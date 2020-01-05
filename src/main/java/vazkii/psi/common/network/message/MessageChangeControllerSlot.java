/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 23:44:41 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageChangeControllerSlot implements IMessage {

	public int controlSlot;
	public int slot;

	public MessageChangeControllerSlot() { }

	public MessageChangeControllerSlot(int controlSlot, int slot) {
		this.controlSlot = controlSlot;
		this.slot = slot;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ServerPlayerEntity player = context.getSender();
			ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
			if(!stack.isEmpty() && stack.getItem() instanceof ISocketableController) {
				((ISocketableController) stack.getItem()).setSelectedSlot(player, stack, controlSlot, slot);
			} else {
				stack = player.getHeldItem(Hand.MAIN_HAND);
				if(!stack.isEmpty() && stack.getItem() instanceof ISocketableController)
					((ISocketableController) stack.getItem()).setSelectedSlot(player, stack, controlSlot, slot);
			}
			PlayerDataHandler.get(player).stopLoopcast();
		});

		return true;
	}

}
