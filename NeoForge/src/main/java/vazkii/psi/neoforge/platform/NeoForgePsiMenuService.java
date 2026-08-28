/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import vazkii.psi.common.platform.PsiMenuService;

public final class NeoForgePsiMenuService implements PsiMenuService {

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createBlockPosMenu(BlockPosMenuFactory<T> factory) {
		return IMenuTypeExtension.create(
				(windowId, inventory, buffer) -> factory.create(windowId, inventory, buffer.readBlockPos()));
	}

	@Override
	public void openBlockPosMenu(Player player, MenuProvider provider, BlockPos pos) {
		player.openMenu(provider, pos);
	}
}
