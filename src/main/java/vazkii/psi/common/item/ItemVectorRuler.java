/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.item.base.IHUDItem;
import vazkii.psi.common.item.base.ModItems;

import java.util.List;

public class ItemVectorRuler extends Item implements IHUDItem {

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
	public InteractionResult useOn(UseOnContext ctx) {
		BlockPos pos = ctx.getClickedPos();

		ItemStack stack = ctx.getPlayer().getItemInHand(ctx.getHand());

		if(!stack.has(ModItems.TAG_SRC_POS) || ctx.getPlayer().isShiftKeyDown()) {
			stack.set(ModItems.TAG_SRC_POS, pos);
			stack.remove(ModItems.TAG_DST_POS);
		} else {
			stack.set(ModItems.TAG_DST_POS, pos);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
		tooltip.add(Component.literal(getVector(stack).toString()));
	}

	public Vector3 getVector(ItemStack stack) {
		BlockPos src = stack.getOrDefault(ModItems.TAG_SRC_POS, BlockPos.ZERO);

		if(!stack.has(ModItems.TAG_DST_POS)) {
			return Vector3.fromBlockPos(src);
		}

		BlockPos dst = stack.getOrDefault(ModItems.TAG_DST_POS, BlockPos.ZERO);

		return Vector3.fromBlockPos(dst.subtract(src));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawHUD(GuiGraphics graphics, float partTicks, int screenWidth, int screenHeight, ItemStack stack) {
		String s = getVector(stack).toString();

		Font font = Minecraft.getInstance().font;
		int w = font.width(s);
		graphics.drawString(font, s, screenWidth / 2f - w / 2f, screenHeight / 2f + 10, 0xFFFFFFFF, false);
	}
}
