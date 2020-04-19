/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
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
		super(properties.maxStackSize(1));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		BlockPos pos = ctx.getPos();

		ItemStack stack = ctx.getPlayer().getHeldItem(ctx.getHand());
		int srcY = stack.getOrCreateTag().contains(TAG_SRC_Y) ? stack.getOrCreateTag().getInt(TAG_SRC_Y) : -1;

		if (srcY == -1 || ctx.getPlayer().isSneaking()) {
			stack.getOrCreateTag().putInt(TAG_SRC_X, pos.getX());
			stack.getOrCreateTag().putInt(TAG_SRC_Y, pos.getY());
			stack.getOrCreateTag().putInt(TAG_SRC_Z, pos.getZ());
			stack.getOrCreateTag().putInt(TAG_DST_Y, -1);
		} else {
			stack.getOrCreateTag().putInt(TAG_DST_X, pos.getX());
			stack.getOrCreateTag().putInt(TAG_DST_Y, pos.getY());
			stack.getOrCreateTag().putInt(TAG_DST_Z, pos.getZ());
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		tooltip.add(new StringTextComponent(getVector(stack).toString()));
	}

	public Vector3 getVector(ItemStack stack) {
		int srcX = stack.getOrCreateTag().getInt(TAG_SRC_X);
		int srcY = stack.getOrCreateTag().getInt(TAG_SRC_Y);
		int srcZ = stack.getOrCreateTag().getInt(TAG_SRC_Z);

		int dstY = stack.getOrCreateTag().contains(TAG_DST_Y) ? stack.getOrCreateTag().getInt(TAG_DST_Y) : -1;
		if (dstY == -1) {
			return new Vector3(srcX, srcY, srcZ);
		}

		int dstX = stack.getOrCreateTag().getInt(TAG_DST_X);
		int dstZ = stack.getOrCreateTag().getInt(TAG_DST_Z);

		return new Vector3(dstX - srcX, dstY - srcY, dstZ - srcZ);
	}

	public static Vector3 getRulerVector(PlayerEntity player) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemVectorRuler) {
				return ((ItemVectorRuler) stack.getItem()).getVector(stack);
			}
		}

		return Vector3.zero;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawHUD(MainWindow res, float partTicks, ItemStack stack) {
		String s = getVector(stack).toString();

		FontRenderer font = Minecraft.getInstance().fontRenderer;
		int w = font.getStringWidth(s);
		font.drawStringWithShadow(s, res.getScaledWidth() / 2f - w / 2f, res.getScaledHeight() / 2f + 10, 0xFFFFFFFF);
	}
}
