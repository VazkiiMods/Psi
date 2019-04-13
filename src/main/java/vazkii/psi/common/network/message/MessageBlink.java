/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Mar 20, 2019, 19:58 AM (EST)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;

public class MessageBlink extends NetworkMessage<MessageBlink> {
	public double offX;
	public double offY;
	public double offZ;

	public MessageBlink() {
		// NO-OP
	}

	public MessageBlink(double offX, double offY, double offZ) {
		this.offX = offX;
		this.offY = offY;
		this.offZ = offZ;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			Entity entity = Minecraft.getMinecraft().player;
			if (entity != null)
				entity.setPosition(entity.posX + offX, entity.posY + offY, entity.posZ + offZ);
		});
		return null;
	}
}
