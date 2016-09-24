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

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class MessageChangeSocketableSlot extends NetworkMessage {

	public int slot;

	public MessageChangeSocketableSlot() { }

	public MessageChangeSocketableSlot(int slot) {
		this.slot = slot;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayerMP player = context.getServerHandler().playerEntity;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if(stack != null && stack.getItem() instanceof ISocketable)
			((ISocketable) stack.getItem()).setSelectedSlot(stack, slot);
		else {
			stack = player.getHeldItem(EnumHand.OFF_HAND);
			if(stack != null && stack.getItem() instanceof ISocketable)
				((ISocketable) stack.getItem()).setSelectedSlot(stack, slot);
		}
		PlayerDataHandler.get(player).stopLoopcast();

		return null;
	}

}
