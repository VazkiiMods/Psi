/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.common.platform.PsiMenuService;

public final class FabricPsiMenuService implements PsiMenuService {

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createBlockPosMenu(BlockPosMenuFactory<T> factory) {
		return new ExtendedScreenHandlerType<>(factory::create, BlockPos.STREAM_CODEC);
	}

	@Override
	public void openBlockPosMenu(Player player, MenuProvider provider, BlockPos pos) {
		player.openMenu(new ExtendedScreenHandlerFactory<BlockPos>() {
			@Override
			public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
				return pos;
			}

			@Override
			public @NotNull net.minecraft.network.chat.Component getDisplayName() {
				return provider.getDisplayName();
			}

			@Override
			public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player menuPlayer) {
				return provider.createMenu(windowId, inventory, menuPlayer);
			}
		});
	}
}
