/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
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

	public MessageAdditiveMotion(FriendlyByteBuf buf) {
		entityID = buf.readVarInt();
		motionX = buf.readDouble();
		motionY = buf.readDouble();
		motionZ = buf.readDouble();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(entityID);
		buf.writeDouble(motionX);
		buf.writeDouble(motionY);
		buf.writeDouble(motionZ);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Level world = Psi.proxy.getClientWorld();
			if (world != null) {
				Entity entity = world.getEntity(entityID);
				if (entity != null) {
					entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
				}
			}
		});

		return true;
	}
}
