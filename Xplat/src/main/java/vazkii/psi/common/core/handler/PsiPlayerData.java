/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import net.minecraft.world.entity.player.Player;

import vazkii.psi.common.platform.PsiPlayerDataStorage;

public final class PsiPlayerData {

	private PsiPlayerData() {}

	public static PlayerData get(Player player) {
		return player == null ? new PlayerData() : PsiPlayerDataStorage.get(player);
	}

}
