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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.*;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.network.message.MessageLoopcastSync;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class LoopcastTrackingHandler {
	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof EntityPlayer)
			syncDataFor((EntityPlayer) event.getTarget(), (EntityPlayerMP) event.getEntityPlayer());
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) {
		syncDataFor(event.player, (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public static void onPlayerLogIn(PlayerLoggedInEvent event) {
		syncDataFor(event.player, (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		syncDataFor(event.player, (EntityPlayerMP) event.player);
	}

	public static void syncDataFor(EntityPlayer player, EntityPlayerMP receiver) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		NetworkHandler.INSTANCE.sendTo(new MessageLoopcastSync(player.getEntityId(), data.loopcasting, data.loopcastHand), receiver);
	}

	public static void syncForTrackers(EntityPlayerMP player) {
		syncDataFor(player, player);

		for (EntityPlayer tracker : player.getServerWorld().getEntityTracker().getTrackingPlayers(player))
			if (tracker instanceof EntityPlayerMP)
				syncDataFor(player, (EntityPlayerMP) tracker);
	}
}
