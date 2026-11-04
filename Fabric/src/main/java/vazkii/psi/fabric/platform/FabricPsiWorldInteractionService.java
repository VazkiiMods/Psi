/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.platform;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.psi.common.platform.PsiWorldInteractionService;

public final class FabricPsiWorldInteractionService implements PsiWorldInteractionService {

	@Override
	public boolean isFakePlayer(Entity entity) {
		return entity instanceof FakePlayer;
	}

	@Override
	public boolean canBreak(Player player, Level level, BlockPos pos, BlockState state) {
		return PlayerBlockBreakEvents.BEFORE.invoker()
				.beforeBlockBreak(level, player, pos, state, level.getBlockEntity(pos));
	}

	@Override
	public boolean canPlace(Player player, Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return PlayerBlockBreakEvents.BEFORE.invoker()
				.beforeBlockBreak(level, player, pos, state, level.getBlockEntity(pos));
	}

	@Override
	public ItemStack craftingRemainingItem(ItemStack stack) {
		Item remainder = stack.getItem().getCraftingRemainingItem();
		return remainder == null ? ItemStack.EMPTY : new ItemStack(remainder);
	}

}
