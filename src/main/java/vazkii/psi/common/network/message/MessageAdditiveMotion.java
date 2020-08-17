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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.common.Psi;

import java.util.function.Supplier;

public class MessageAdditiveMotion {

	private final int entityID;
	private final int motionX;
	private final int motionY;
	private final int motionZ;

	public MessageAdditiveMotion(int entityID, double motionX, double motionY, double motionZ) {
		this.entityID = entityID;

		this.motionX = (int) (MathHelper.clamp(motionX, -3.9, 3.9) * 8000);
		this.motionY = (int) (MathHelper.clamp(motionY, -3.9, 3.9) * 8000);
		this.motionZ = (int) (MathHelper.clamp(motionZ, -3.9, 3.9) * 8000);
	}

	public MessageAdditiveMotion(PacketBuffer buf) {
		entityID = buf.readVarInt();
		motionX = buf.readInt();
		motionY = buf.readInt();
		motionZ = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeVarInt(entityID);
		buf.writeInt(motionX);
		buf.writeInt(motionY);
		buf.writeInt(motionZ);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			World world = Psi.proxy.getClientWorld();
			if (world != null) {
				Entity entity = world.getEntityByID(entityID);
				if (entity != null) {
					entity.setMotion(entity.getMotion().add(motionX / 8000.0, motionY / 8000.0, motionZ / 8000.0));
				}
			}
		});

		return true;
	}
}
