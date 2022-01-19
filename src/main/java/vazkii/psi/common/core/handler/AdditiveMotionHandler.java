/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageAdditiveMotion;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class AdditiveMotionHandler {
	private static final Map<Entity, Vector3d> toUpdate = new WeakHashMap<>();

	public static void addMotion(Entity entity, double x, double y, double z) {
		if (x == 0 && y == 0 && z == 0) {
			return;
		}
		if (!entity.level.isClientSide) {
			Vector3d base = toUpdate.getOrDefault(entity, Vector3d.ZERO);
			toUpdate.put(entity, base.add(x, y, z));
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.WorldTickEvent e) {
		if (e.side.isServer() && e.phase == TickEvent.Phase.END) {
			for (Entity entity : toUpdate.keySet()) {
				if (!entity.hurtMarked) { // Allow velocity change packets to take priority.
					Vector3d vec = toUpdate.get(entity);
					if (vec != null) { // Edge case where the entity expired in the ms between calls
						MessageAdditiveMotion motion = new MessageAdditiveMotion(entity.getId(), vec.x, vec.y, vec.z);
						//We want a player's motion to be handled client-side to ensure movement consistency
						//Otherwise it feels jerky.
						if (entity instanceof ServerPlayerEntity) {
							MessageRegister.sendToPlayer(motion, (ServerPlayerEntity) entity);
						} else {
							entity.push(vec.x, vec.y, vec.z);
						}
						if (entity.level instanceof ServerWorld) {
							MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), motion);
						}

					}
				}
			}

			toUpdate.clear();
		}
	}
}
