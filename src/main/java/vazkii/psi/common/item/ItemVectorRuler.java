/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [22/02/2016, 15:30:13 (GMT)]
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
import vazkii.arl.item.BasicItem;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.item.base.IHUDItem;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVectorRuler extends BasicItem implements IHUDItem {

	private static final String TAG_SRC_X = "srcX";
	private static final String TAG_SRC_Y = "srcY";
	private static final String TAG_SRC_Z = "srcZ";

	private static final String TAG_DST_X = "dstX";
	private static final String TAG_DST_Y = "dstY";
	private static final String TAG_DST_Z = "dstZ";

	public ItemVectorRuler(String name, Item.Properties properties) {
		super(name, properties.maxStackSize(1));
	}


	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		BlockPos pos = ctx.getPos();

		ItemStack stack = ctx.getPlayer().getHeldItem(ctx.getHand());
		int srcY = ItemNBTHelper.getInt(stack, TAG_SRC_Y, -1);

		if (srcY == -1 || ctx.getPlayer().isSneaking()) {
			ItemNBTHelper.setInt(stack, TAG_SRC_X, pos.getX());
			ItemNBTHelper.setInt(stack, TAG_SRC_Y, pos.getY());
			ItemNBTHelper.setInt(stack, TAG_SRC_Z, pos.getZ());
			ItemNBTHelper.setInt(stack, TAG_DST_Y, -1);
		} else {
			ItemNBTHelper.setInt(stack, TAG_DST_X, pos.getX());
			ItemNBTHelper.setInt(stack, TAG_DST_Y, pos.getY());
			ItemNBTHelper.setInt(stack, TAG_DST_Z, pos.getZ());
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		tooltip.add(new StringTextComponent(getVector(stack).toString()));
	}

	public Vector3 getVector(ItemStack stack) {
		int srcX = ItemNBTHelper.getInt(stack, TAG_SRC_X, 0);
		int srcY = ItemNBTHelper.getInt(stack, TAG_SRC_Y, 0);
		int srcZ = ItemNBTHelper.getInt(stack, TAG_SRC_Z, 0);
		
		int dstY = ItemNBTHelper.getInt(stack, TAG_DST_Y, -1);
		if(dstY == -1)
			return new Vector3(srcX, srcY, srcZ);

		int dstX = ItemNBTHelper.getInt(stack, TAG_DST_X, 0);
		int dstZ = ItemNBTHelper.getInt(stack, TAG_DST_Z, 0);
		
		return new Vector3(dstX - srcX, dstY - srcY, dstZ - srcZ);
	}
	
	public static Vector3 getRulerVector(PlayerEntity player) {
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemVectorRuler)
				return ((ItemVectorRuler) stack.getItem()).getVector(stack);
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
