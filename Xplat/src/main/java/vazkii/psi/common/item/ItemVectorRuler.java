/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.item.base.ModDataComponents;

import java.util.List;

public class ItemVectorRuler extends Item {

	public ItemVectorRuler(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	public static Vector3 getRulerVector(Player player) {
		for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemVectorRuler) {
				return ((ItemVectorRuler) stack.getItem()).getVector(stack);
			}
		}

		return Vector3.zero;
	}

	@Override
	public @NotNull InteractionResult useOn(UseOnContext ctx) {
		BlockPos pos = ctx.getClickedPos();

		if(ctx.getPlayer() == null) {
			return InteractionResult.FAIL;
		}

		ItemStack stack = ctx.getPlayer().getItemInHand(ctx.getHand());

		if(!stack.has(ModDataComponents.SRC_POS.get()) || ctx.getPlayer().isShiftKeyDown()) {
			stack.set(ModDataComponents.SRC_POS.get(), pos);
			stack.remove(ModDataComponents.DST_POS.get());
		} else {
			stack.set(ModDataComponents.DST_POS.get(), pos);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, @NotNull TooltipFlag advanced) {
		tooltip.add(Component.literal(getVector(stack).toString()));
	}

	public Vector3 getVector(ItemStack stack) {
		BlockPos src = stack.getOrDefault(ModDataComponents.SRC_POS.get(), BlockPos.ZERO);

		if(!stack.has(ModDataComponents.DST_POS.get())) {
			return Vector3.fromBlockPos(src);
		}

		BlockPos dst = stack.getOrDefault(ModDataComponents.DST_POS.get(), BlockPos.ZERO);

		return Vector3.fromBlockPos(dst.subtract(src));
	}

}
