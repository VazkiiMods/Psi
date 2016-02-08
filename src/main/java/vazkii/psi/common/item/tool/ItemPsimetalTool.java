/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [06/02/2016, 20:09:22 (GMT)]
 */
package vazkii.psi.common.item.tool;

import java.util.List;
import java.util.Set;

import ibxm.Player;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ItemModTool;
import vazkii.psi.common.item.base.ModItems;

public class ItemPsimetalTool extends ItemModTool implements IPsimetalTool {

	private static final String TAG_REGEN_TIME = "regenTime";
	
	protected ItemPsimetalTool(String name, float attackDamage, Set<Block> effectiveBlocks, String... variants) {
		super(name, attackDamage, PsiAPI.PSIMETAL_MATERIAL, effectiveBlocks, variants);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		super.onBlockStartBreak(itemstack, pos, player);
	
		PlayerData data = PlayerDataHandler.get(player);
		ItemStack playerCad = PsiAPI.getPlayerCAD(player);

		if(playerCad != null) {
			ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
			ItemCAD.cast(player.worldObj, player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
				context.tool = itemstack;
				context.positionBroken = raytraceFromEntity(player.worldObj, player, false, 4.5);
			});	
		}
		
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && !isSelected) {
			int regenTime = ItemNBTHelper.getInt(stack, TAG_REGEN_TIME, 0);
			if(regenTime % 80 == 0) {
				EntityPlayer player = (EntityPlayer) entityIn;
				PlayerData data = PlayerDataHandler.get(player);
				data.deductPsi(600, 5, true);
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
			ItemNBTHelper.setInt(stack, TAG_REGEN_TIME, regenTime + 1);
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
		ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.material && par2ItemStack.getItemDamage() == 1 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean par3, double range) {
		float f = 1.0F;
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
		double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
		if (player instanceof EntityPlayer)
			d1 += ((EntityPlayer) player).eyeHeight;
		double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
		Vec3 vec3 = new Vec3(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
		return world.rayTraceBlocks(vec3, vec31, par3);
	}
	
}
