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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class PsiWorldInteractions {

	private static final PsiWorldInteractionService SERVICE = PsiServices.load(PsiWorldInteractionService.class);

	private PsiWorldInteractions() {}

	public static boolean isFakePlayer(Entity entity) {
		return SERVICE.isFakePlayer(entity);
	}

	public static boolean canBreak(Player player, Level level, BlockPos pos, BlockState state) {
		return SERVICE.canBreak(player, level, pos, state);
	}

	public static boolean canPlace(Player player, Level level, BlockPos pos) {
		return SERVICE.canPlace(player, level, pos);
	}

	public static ItemStack craftingRemainingItem(ItemStack stack) {
		return SERVICE.craftingRemainingItem(stack);
	}

}
