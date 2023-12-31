/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.item.base.IHUDItem;

import javax.annotation.Nullable;

import java.util.List;

public class ItemVectorRuler extends Item implements IHUDItem {

	private static final String TAG_SRC_X = "srcX";
	private static final String TAG_SRC_Y = "srcY";
	private static final String TAG_SRC_Z = "srcZ";

	private static final String TAG_DST_X = "dstX";
	private static final String TAG_DST_Y = "dstY";
	private static final String TAG_DST_Z = "dstZ";

	public ItemVectorRuler(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		BlockPos pos = ctx.getClickedPos();

		ItemStack stack = ctx.getPlayer().getItemInHand(ctx.getHand());
		int srcY = stack.getOrCreateTag().contains(TAG_SRC_Y) ? stack.getOrCreateTag().getInt(TAG_SRC_Y) : -1;

		if(srcY == -1 || ctx.getPlayer().isShiftKeyDown()) {
			stack.getOrCreateTag().putInt(TAG_SRC_X, pos.getX());
			stack.getOrCreateTag().putInt(TAG_SRC_Y, pos.getY());
			stack.getOrCreateTag().putInt(TAG_SRC_Z, pos.getZ());
			stack.getOrCreateTag().putInt(TAG_DST_Y, -1);
		} else {
			stack.getOrCreateTag().putInt(TAG_DST_X, pos.getX());
			stack.getOrCreateTag().putInt(TAG_DST_Y, pos.getY());
			stack.getOrCreateTag().putInt(TAG_DST_Z, pos.getZ());
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
		tooltip.add(new TextComponent(getVector(stack).toString()));
	}

	public Vector3 getVector(ItemStack stack) {
		int srcX = stack.getOrCreateTag().getInt(TAG_SRC_X);
		int srcY = stack.getOrCreateTag().getInt(TAG_SRC_Y);
		int srcZ = stack.getOrCreateTag().getInt(TAG_SRC_Z);

		int dstY = stack.getOrCreateTag().contains(TAG_DST_Y) ? stack.getOrCreateTag().getInt(TAG_DST_Y) : -1;
		if(dstY == -1) {
			return new Vector3(srcX, srcY, srcZ);
		}

		int dstX = stack.getOrCreateTag().getInt(TAG_DST_X);
		int dstZ = stack.getOrCreateTag().getInt(TAG_DST_Z);

		return new Vector3(dstX - srcX, dstY - srcY, dstZ - srcZ);
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
	@OnlyIn(Dist.CLIENT)
	public void drawHUD(PoseStack ms, Window res, float partTicks, ItemStack stack) {
		String s = getVector(stack).toString();

		Font font = Minecraft.getInstance().font;
		int w = font.width(s);
		font.draw(ms, s, res.getGuiScaledWidth() / 2f - w / 2f, res.getGuiScaledHeight() / 2f + 10, 0xFFFFFFFF);
	}
}
