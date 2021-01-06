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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageAdditiveMotion {

	private final int entityID;
	private final double motionX;
	private final double motionY;
	private final double motionZ;

	public MessageAdditiveMotion(int entityID, double motionX, double motionY, double motionZ) {
		this.entityID = entityID;

		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public MessageAdditiveMotion(PacketBuffer buf) {
		entityID = buf.readVarInt();
		motionX = buf.readDouble();
		motionY = buf.readDouble();
		motionZ = buf.readDouble();
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(entityID);
		buf.writeDouble(motionX);
		buf.writeDouble(motionY);
		buf.writeDouble(motionZ);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			World world = Psi.proxy.getClientWorld();
			if (world != null) {
				Entity entity = world.getEntityByID(entityID);
				if (entity != null) {
					entity.setMotion(entity.getMotion().add(motionX, motionY, motionZ));
				}
			}
		});

		return true;
	}
}
