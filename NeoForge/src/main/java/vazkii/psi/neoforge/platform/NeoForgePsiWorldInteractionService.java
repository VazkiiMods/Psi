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
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;

import vazkii.psi.common.platform.PsiWorldInteractionService;

public final class NeoForgePsiWorldInteractionService implements PsiWorldInteractionService {

	@Override
	public boolean isFakePlayer(Entity entity) {
		return entity instanceof FakePlayer;
	}

	@Override
	public boolean canBreak(Player player, Level level, BlockPos pos, BlockState state) {
		return !NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player)).isCanceled();
	}

	@Override
	public boolean canPlace(Player player, Level level, BlockPos pos) {
		BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos);
		BlockEvent.EntityPlaceEvent event = new BlockEvent.EntityPlaceEvent(
				snapshot, level.getBlockState(pos.relative(Direction.UP)), player);
		return !NeoForge.EVENT_BUS.post(event).isCanceled();
	}

	@Override
	public ItemStack craftingRemainingItem(ItemStack stack) {
		return CommonHooks.getCraftingRemainingItem(stack);
	}

}
