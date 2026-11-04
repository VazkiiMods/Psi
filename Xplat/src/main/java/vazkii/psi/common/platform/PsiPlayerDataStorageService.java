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

public interface PsiPlayerDataStorageService {

	void initialize();

	PlayerData get(Player player);

	void save(Player player, PlayerData data);

}
