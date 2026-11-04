/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

import vazkii.psi.common.network.PsiNetwork;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.MessageDeductPsi;
import vazkii.psi.common.network.message.MessageEidosSync;
import vazkii.psi.common.network.message.MessageLoopcastSync;
import vazkii.psi.common.network.message.MessagePsiOverflow;

public final class PsiPlayerDataSync {

	private PsiPlayerDataSync() {}

	public static void sendFull(ServerPlayer player, CompoundTag data) {
		PsiNetwork.sendToPlayer(player, new MessageDataSync(data));
	}

	public static void sendDeduction(ServerPlayer player, int previous, int current, int cooldown, boolean shatter) {
		PsiNetwork.sendToPlayer(player, new MessageDeductPsi(previous, current, cooldown, shatter));
	}

	public static void sendOverflow(ServerPlayer player, boolean overflowed) {
		PsiNetwork.sendToPlayer(player, new MessagePsiOverflow(overflowed));
	}

	public static void sendLoopcast(ServerPlayer player, boolean loopcasting, InteractionHand hand) {
		MessageLoopcastSync message = new MessageLoopcastSync(player.getId(), loopcasting, hand);
		PsiNetwork.sendToPlayersTracking(player, message);
		PsiNetwork.sendToPlayer(player, message);
	}

	public static void sendEidosReversion(ServerPlayer player, int reversionTime) {
		PsiNetwork.sendToPlayer(player, new MessageEidosSync(reversionTime));
	}

}
