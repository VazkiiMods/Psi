/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Mar 15, 2019, 10:57 AM (EST)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.api.distmarker.Dist;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.message.MessageAdditiveMotion;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class AdditiveMotionHandler {
	private static Map<Entity, Vec3d> toUpdate = new WeakHashMap<>();

	public static void addMotion(Entity entity, double x, double y, double z) {
		if (x == 0 && y == 0 && z == 0) return;

		entity.motionX += x;
		entity.motionY += y;
		entity.motionZ += z;

		if (!entity.world.isRemote) {
			Vec3d base = toUpdate.getOrDefault(entity, Vec3d.ZERO);

			toUpdate.put(entity, base.add(x, y, z));
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.WorldTickEvent e) {
		if (e.side == Dist.SERVER && e.phase == TickEvent.Phase.END) {
			for (Entity entity : toUpdate.keySet()) {
				if (!entity.velocityChanged) { // Allow velocity change packets to take priority.
					Vec3d vec = toUpdate.get(entity);
					if (vec != null) { // Edge case where the entity expired in the ms between calls
						MessageAdditiveMotion motion = new MessageAdditiveMotion(entity.getEntityId(), vec.x, vec.y, vec.z);

						if (entity instanceof ServerPlayerEntity)
							NetworkHandler.INSTANCE.sendTo(motion, (ServerPlayerEntity) entity);

						if (entity.world instanceof ServerWorld) {
							ServerWorld server = ((ServerWorld) entity.world);
							for (PlayerEntity player : server.getEntityTracker().getTrackingPlayers(entity)) {
								if (player != entity && player instanceof ServerPlayerEntity)
									NetworkHandler.INSTANCE.sendTo(motion, (ServerPlayerEntity) player);
							}
						}
					}
				}
			}

			toUpdate.clear();
		}
	}
}
