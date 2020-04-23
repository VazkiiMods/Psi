package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.common.Psi;

public class MessageParticleTrail implements IMessage {
	private static final int STEPS_PER_UNIT = 4;

	public Vec3d position;
	public Vec3d direction;
	public double length;
	public int time;
	public ItemStack cad;

	public MessageParticleTrail(){
		//NO-OP
	}

	public MessageParticleTrail(Vec3d position, Vec3d direction, double length, int time, ItemStack cad) {
		this.position = position;
		this.direction = direction;
		this.length = length;
		this.time = time;
		this.cad = cad;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			World world = Minecraft.getInstance().world;

			int color = Psi.proxy.getColorForCAD(cad);

			float red = PsiRenderHelper.r(color);
			float green = PsiRenderHelper.g(color);
			float blue = PsiRenderHelper.b(color);

			Vec3d ray = direction.normalize().scale(1f / STEPS_PER_UNIT);
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
