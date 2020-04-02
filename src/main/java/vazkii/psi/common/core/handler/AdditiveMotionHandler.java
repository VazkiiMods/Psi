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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
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
	private static final Map<Entity, Vec3d> toUpdate = new WeakHashMap<>();

	public static void addMotion(Entity entity, double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) return;

        entity.addVelocity(x, y, z);

        if (!entity.world.isRemote) {
            Vec3d base = toUpdate.getOrDefault(entity, Vec3d.ZERO);

            toUpdate.put(entity, base.add(x, y, z));
        }
    }


    @SubscribeEvent
	public static void onPlayerTick(TickEvent.WorldTickEvent e) {
        if (e.side.isServer() && e.phase == TickEvent.Phase.END) {
            for (Entity entity : toUpdate.keySet()) {
                if (!entity.velocityChanged) { // Allow velocity change packets to take priority.
                    Vec3d vec = toUpdate.get(entity);
                    if (vec != null) { // Edge case where the entity expired in the ms between calls
                        MessageAdditiveMotion motion = new MessageAdditiveMotion(entity.getEntityId(), vec.x, vec.y, vec.z);

                        if (entity instanceof ServerPlayerEntity)
                            MessageRegister.HANDLER.sendToPlayer(motion, (ServerPlayerEntity) entity);

                        if (entity.world instanceof ServerWorld) {
                            MessageRegister.HANDLER.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), motion);
                        }
					}
				}
			}

			toUpdate.clear();
		}
	}
}
