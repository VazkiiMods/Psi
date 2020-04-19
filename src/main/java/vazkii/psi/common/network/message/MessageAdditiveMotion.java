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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;

public class MessageAdditiveMotion implements IMessage {

	public int entityID;
	public int motionX;
	public int motionY;
	public int motionZ;

	public MessageAdditiveMotion() {
		// NO-OP
	}

	public MessageAdditiveMotion(int entityID, double motionX, double motionY, double motionZ) {
		this.entityID = entityID;

		this.motionX = (int) (MathHelper.clamp(motionX, -3.9, 3.9) * 8000);
		this.motionY = (int) (MathHelper.clamp(motionY, -3.9, 3.9) * 8000);
		this.motionZ = (int) (MathHelper.clamp(motionZ, -3.9, 3.9) * 8000);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			World world = Minecraft.getInstance().world;
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
