/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;

public class MessageBlink implements IMessage {
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
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			Entity entity = Minecraft.getInstance().player;
			if (entity != null) {
				entity.setPosition(entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ);
			}
		});
		return true;
	}
}
