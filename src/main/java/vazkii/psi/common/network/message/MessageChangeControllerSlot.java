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

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.api.cad.ISocketableController;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.Message;

public class MessageChangeControllerSlot extends Message {

	public int controlSlot;
	public int slot;

	public MessageChangeControllerSlot() { }

	public MessageChangeControllerSlot(int controlSlot, int slot) {
		this.controlSlot = controlSlot;
		this.slot = slot;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayerMP player = context.getServerHandler().playerEntity;
		ItemStack stack = player.getCurrentEquippedItem();

		if(stack != null && stack.getItem() instanceof ISocketableController)
			((ISocketableController) stack.getItem()).setSelectedSlot(player, stack, controlSlot, slot);
		PlayerDataHandler.get(player).stopLoopcast();

		return null;
	}

}
