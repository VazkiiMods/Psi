/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
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

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.Psi;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageAdditiveMotion;

import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = PsiAPI.MOD_ID)
public class AdditiveMotionHandler {
	private static final Map<Entity, MotionUpdate> toUpdate = new WeakHashMap<>();

	public static void addMotion(Entity entity, double x, double y, double z) {
		addMotion(entity, x, y, z, false);
	}

	public static void addMotion(Entity entity, double x, double y, double z, boolean predictionEligible) {
		if(x == 0 && y == 0 && z == 0) {
			return;
		}
		Vec3 motion = new Vec3(x, y, z);
		if(entity.level().isClientSide) {
			if(predictionEligible) {
				entity.push(x, y, z);
				Psi.proxy.recordPredictedMotion(motion);
			}
		} else {
			MotionUpdate update = toUpdate.getOrDefault(entity, MotionUpdate.EMPTY);
			toUpdate.put(entity, update.add(motion, predictionEligible));
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(LevelTickEvent.Post e) {
		if(!e.getLevel().isClientSide()) {
			for(Entity entity : toUpdate.keySet()) {
				if(!entity.hurtMarked) { // Allow velocity change packets to take priority.
					MotionUpdate update = toUpdate.get(entity);
					if(update != null) { // Edge case where the entity expired in the ms between calls
						Vec3 total = update.predicted.add(update.unpredicted);
						//We want a player's motion to be handled client-side to ensure movement consistency
						//Otherwise it feels jerky.
						if(entity instanceof ServerPlayer player) {
							player.connection.aboveGroundTickCount += -2 * getMaximumFlyingTicks(entity);
							if(!update.predicted.equals(Vec3.ZERO)) {
								MessageRegister.sendToPlayer(player, new MessageAdditiveMotion(entity.getId(), update.predicted.x, update.predicted.y, update.predicted.z, true));
							}
							if(!update.unpredicted.equals(Vec3.ZERO)) {
								MessageRegister.sendToPlayer(player, new MessageAdditiveMotion(entity.getId(), update.unpredicted.x, update.unpredicted.y, update.unpredicted.z, false));
							}
							player.connection.aboveGroundTickCount += -2 * getMaximumFlyingTicks(entity);
						} else {
							entity.push(total.x, total.y, total.z);
						}
						if(entity.level() instanceof ServerLevel) {
							MessageRegister.sendToPlayersTrackingEntity(entity, new MessageAdditiveMotion(entity.getId(), total.x, total.y, total.z, false));
						}

					}
				}
			}

			toUpdate.clear();
		}
	}

	/**
	 * [VanillaCopy] of {@see net.minecraft.server.network.ServerGamePacketListenerImpl#getMaximumFlyingTicks}
	 * but without the extra processing and endpoint bumping
	 */

	private static int getMaximumFlyingTicks(Entity entity) {
		double d0 = entity.getGravity();
		if(d0 < 1.0E-5F) {
			return Integer.MAX_VALUE;
		} else {
			double d1 = 0.08 / d0;
			return Mth.ceil(80.0 * Math.max(d1, 1.0));
		}
	}

	private record MotionUpdate(Vec3 predicted, Vec3 unpredicted) {
		private static final MotionUpdate EMPTY = new MotionUpdate(Vec3.ZERO, Vec3.ZERO);

		private MotionUpdate add(Vec3 motion, boolean predictionEligible) {
			return predictionEligible
					? new MotionUpdate(predicted.add(motion), unpredicted)
					: new MotionUpdate(predicted, unpredicted.add(motion));
		}
	}

}
