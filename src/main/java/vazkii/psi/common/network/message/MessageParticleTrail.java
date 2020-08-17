/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.network.MessageRegister;

import java.util.function.Supplier;

public class MessageParticleTrail {
	private static final int STEPS_PER_UNIT = 4;

	private final Vector3d position;
	private final Vector3d direction;
	private final double length;
	private final int time;
	private final ItemStack cad;

	public MessageParticleTrail(Vector3d position, Vector3d direction, double length, int time, ItemStack cad) {
		this.position = position;
		this.direction = direction;
		this.length = length;
		this.time = time;
		this.cad = cad;
	}

	public MessageParticleTrail(PacketBuffer buf) {
		this.position = MessageRegister.readVec3d(buf);
		this.direction = MessageRegister.readVec3d(buf);
		this.length = buf.readDouble();
		this.time = buf.readVarInt();
		this.cad = buf.readItemStack();
	}

	public void encode(PacketBuffer buf) {
		MessageRegister.writeVec3d(buf, position);
		MessageRegister.writeVec3d(buf, direction);
		buf.writeDouble(length);
		buf.writeVarInt(time);
		buf.writeItemStack(cad);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			World world = Psi.proxy.getClientWorld();

			int color = Psi.proxy.getColorForCAD(cad);

			float red = PsiRenderHelper.r(color);
			float green = PsiRenderHelper.g(color);
			float blue = PsiRenderHelper.b(color);

			Vector3d ray = direction.normalize().scale(1f / STEPS_PER_UNIT);
			int steps = (int) (length * STEPS_PER_UNIT);

			for (int i = 0; i < steps; i++) {
				double x = position.x + ray.x * i;
				double y = position.y + ray.y * i;
				double z = position.z + ray.z * i;

				Psi.proxy.sparkleFX(world, x, y, z, red, green, blue, 0, 0, 0, 1f, time);
			}
		});
		return true;
	}
}
