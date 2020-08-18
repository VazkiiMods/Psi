/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;

import java.util.function.Supplier;

/**
 * This is needed instead of a serverside position set to avoid jittering, especially under lag.
 */
public class MessageBlink {
	private final double offX;
	private final double offY;
	private final double offZ;

	public MessageBlink(double offX, double offY, double offZ) {
		this.offX = offX;
		this.offY = offY;
		this.offZ = offZ;
	}

	public MessageBlink(PacketBuffer buf) {
		this.offX = buf.readDouble();
		this.offY = buf.readDouble();
		this.offZ = buf.readDouble();
	}

	public void encode(PacketBuffer buf) {
		buf.writeDouble(offX);
		buf.writeDouble(offY);
		buf.writeDouble(offZ);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Entity entity = Psi.proxy.getClientPlayer();
			if (entity != null) {
				entity.setPosition(entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ);
			}
		});
		return true;
	}
}
