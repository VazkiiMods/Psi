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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public final class PsiMenus {
	private static final PsiMenuService SERVICE = PsiServices.load(PsiMenuService.class);

	private PsiMenus() {}

	public static <T extends AbstractContainerMenu> MenuType<T> createBlockPosMenu(
			PsiMenuService.BlockPosMenuFactory<T> factory) {
		return SERVICE.createBlockPosMenu(factory);
	}

	public static void openBlockPosMenu(Player player, MenuProvider provider, BlockPos pos) {
		SERVICE.openBlockPosMenu(player, provider, pos);
	}
}
