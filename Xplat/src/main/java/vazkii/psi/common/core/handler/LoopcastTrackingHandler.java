/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageLoopcastSync;

public final class LoopcastTrackingHandler {
	private LoopcastTrackingHandler() {}

	public static void syncDataFor(Player player, ServerPlayer receiver) {
		PlayerData data = PsiPlayerData.get(player);
		PsiNetwork.sendToPlayer(receiver,
				new MessageLoopcastSync(player.getId(), data.loopcasting, data.loopcastHand));
	}

	public static void syncForTrackersAndSelf(ServerPlayer player) {
		PlayerData data = PsiPlayerData.get(player);
		MessageLoopcastSync message = new MessageLoopcastSync(
				player.getId(), data.loopcasting, data.loopcastHand);
		PsiNetwork.sendToPlayersTracking(player, message);
		PsiNetwork.sendToPlayer(player, message);
	}
}
