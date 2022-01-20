/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.network.message;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;
import vazkii.psi.common.network.MessageRegister;

import java.util.function.Supplier;

public class MessageParticleTrail {
	private static final int STEPS_PER_UNIT = 4;

	private final Vec3 position;
	private final Vec3 direction;
	private final double length;
	private final int time;
	private final ItemStack cad;

	public MessageParticleTrail(Vec3 position, Vec3 direction, double length, int time, ItemStack cad) {
		this.position = position;
		this.direction = direction;
		this.length = length;
		this.time = time;
		this.cad = cad;
	}

	public MessageParticleTrail(FriendlyByteBuf buf) {
		this.position = MessageRegister.readVec3d(buf);
		this.direction = MessageRegister.readVec3d(buf);
		this.length = buf.readDouble();
		this.time = buf.readVarInt();
		this.cad = buf.readItem();
	}

	public void encode(FriendlyByteBuf buf) {
		MessageRegister.writeVec3d(buf, position);
		MessageRegister.writeVec3d(buf, direction);
		buf.writeDouble(length);
		buf.writeVarInt(time);
		buf.writeItem(cad);
	}

	public boolean receive(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Level world = Psi.proxy.getClientWorld();

			int color = Psi.proxy.getColorForCAD(cad);

			float red = PsiRenderHelper.r(color);
			float green = PsiRenderHelper.g(color);
			float blue = PsiRenderHelper.b(color);

			Vec3 ray = direction.normalize().scale(1f / STEPS_PER_UNIT);
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
