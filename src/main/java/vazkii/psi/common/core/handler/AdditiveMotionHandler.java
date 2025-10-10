/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageAdditiveMotion;

import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class AdditiveMotionHandler {
	private static final Map<Entity, Vec3> toUpdate = new WeakHashMap<>();

	public static void addMotion(Entity entity, double x, double y, double z) {
		if(x == 0 && y == 0 && z == 0) {
			return;
		}
		if(!entity.level().isClientSide) {
			Vec3 base = toUpdate.getOrDefault(entity, Vec3.ZERO);
			toUpdate.put(entity, base.add(x, y, z));
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(LevelTickEvent.Post e) {
		if(!e.getLevel().isClientSide()) {
			for(Entity entity : toUpdate.keySet()) {
				if(!entity.hurtMarked) { // Allow velocity change packets to take priority.
					Vec3 vec = toUpdate.get(entity);
					if(vec != null) { // Edge case where the entity expired in the ms between calls
						MessageAdditiveMotion motion = new MessageAdditiveMotion(entity.getId(), vec.x, vec.y, vec.z);
						//We want a player's motion to be handled client-side to ensure movement consistency
						//Otherwise it feels jerky.
						if(entity instanceof ServerPlayer) {
							((ServerPlayer) entity).connection.aboveGroundTickCount += - 2 * getMaximumFlyingTicks(entity);
							MessageRegister.sendToPlayer((ServerPlayer) entity, motion);
							((ServerPlayer) entity).connection.aboveGroundTickCount += - 2 * getMaximumFlyingTicks(entity);
						} else {
							entity.push(vec.x, vec.y, vec.z);
						}
						if(entity.level() instanceof ServerLevel) {
							MessageRegister.sendToPlayersTrackingEntity(entity, motion);
						}

					}
				}
			}

			toUpdate.clear();
		}
	}

	/**
	 * [VanillaCopy] of {@linkplain  net.minecraft.server.network.ServerGamePacketListenerImpl#getMaximumFlyingTicks}
	 * but without the extra processing and endpoint bumping
	 */

	private static int getMaximumFlyingTicks(Entity entity) {
		double d0 = entity.getGravity();
		if (d0 < 1.0E-5F) {
			return Integer.MAX_VALUE;
		} else {
			double d1 = 0.08 / d0;
			return Mth.ceil(80.0 * Math.max(d1, 1.0));
		}
	}

}
