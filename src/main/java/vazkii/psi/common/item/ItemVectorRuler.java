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

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.collection.parallel.ParIterableLike.Min;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.base.IHUDItem;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.lib.LibItemNames;

public class ItemVectorRuler extends ItemMod implements IHUDItem {

	private static final String TAG_SRC_X = "srcX";
	private static final String TAG_SRC_Y = "srcY";
	private static final String TAG_SRC_Z = "srcZ";
	
	private static final String TAG_DST_X = "dstX";
	private static final String TAG_DST_Y = "dstY";
	private static final String TAG_DST_Z = "dstZ";
	
	public ItemVectorRuler() {
		super(LibItemNames.VECTOR_RULER);
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		int srcY = ItemNBTHelper.getInt(stack, TAG_SRC_Y, -1);
		
		if(srcY == -1 || playerIn.isSneaking()) {
			ItemNBTHelper.setInt(stack, TAG_SRC_X, pos.getX());
			ItemNBTHelper.setInt(stack, TAG_SRC_Y, pos.getY());
			ItemNBTHelper.setInt(stack, TAG_SRC_Z, pos.getZ());
			ItemNBTHelper.setInt(stack, TAG_DST_Y, -1);
		} else {
			ItemNBTHelper.setInt(stack, TAG_DST_X, pos.getX());
			ItemNBTHelper.setInt(stack, TAG_DST_Y, pos.getY());
			ItemNBTHelper.setInt(stack, TAG_DST_Z, pos.getZ());
		}
		
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(getVector(stack).toString());
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
	
	public static Vector3 getRulerVector(EntityPlayer player) {
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null && stack.getItem() != null && stack.getItem() instanceof ItemVectorRuler)
				return ((ItemVectorRuler) stack.getItem()).getVector(stack);
		}
		
		return Vector3.zero;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawHUD(ScaledResolution res, float partTicks, ItemStack stack) {
		String s = getVector(stack).toString();
		
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		int w = font.getStringWidth(s);
		font.drawStringWithShadow(s, res.getScaledWidth() / 2 - w / 2, res.getScaledHeight() / 2 + 10, 0xFFFFFFFF);
	}
}
