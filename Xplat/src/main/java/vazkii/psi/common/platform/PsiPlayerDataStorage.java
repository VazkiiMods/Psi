/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.world.entity.player.Player;

import vazkii.psi.common.core.handler.PlayerData;

public final class PsiPlayerDataStorage {

	private static final PsiPlayerDataStorageService SERVICE = PsiServices.load(PsiPlayerDataStorageService.class);

	private PsiPlayerDataStorage() {}

	public static void initialize() {
		SERVICE.initialize();
	}

	public static PlayerData get(Player player) {
		return SERVICE.get(player);
	}

	public static void save(Player player, PlayerData data) {
		SERVICE.save(player, data);
	}

}
