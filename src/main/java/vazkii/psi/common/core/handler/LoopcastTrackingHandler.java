/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * <p>
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * <p>
 * File Created @ [Feb 02, 2019, 09:49 AM (EST)]
 */
package vazkii.psi.common.core.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageLoopcastSync;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class LoopcastTrackingHandler {
	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof PlayerEntity)
            syncDataFor((PlayerEntity) event.getTarget(), (ServerPlayerEntity) event.getPlayer());
	}

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncDataFor(event.getPlayer(), (ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncDataFor(event.getPlayer(), (ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncDataFor(event.getPlayer(), (ServerPlayerEntity) event.getPlayer());
    }

	public static void syncDataFor(PlayerEntity player, ServerPlayerEntity receiver) {
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        MessageRegister.HANDLER.sendToPlayer(new MessageLoopcastSync(player.getEntityId(), data.loopcasting, data.loopcastHand), receiver);
    }

	public static void syncForTrackers(ServerPlayerEntity player) {

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        MessageRegister.HANDLER.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageLoopcastSync(player.getEntityId(), data.loopcasting, data.loopcastHand));

    }
}
