/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface PsiMenuService {

	<T extends AbstractContainerMenu> MenuType<T> createBlockPosMenu(BlockPosMenuFactory<T> factory);

	void openBlockPosMenu(Player player, MenuProvider provider, BlockPos pos);

	@FunctionalInterface
	interface BlockPosMenuFactory<T extends AbstractContainerMenu> {
		T create(int windowId, Inventory inventory, BlockPos pos);
	}
}
