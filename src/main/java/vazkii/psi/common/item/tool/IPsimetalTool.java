/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 21:14:58 (GMT)]
 */
package vazkii.psi.common.item.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellSettable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

public interface IPsimetalTool extends ISocketable, ISpellSettable {

	String TAG_REGEN_TIME = "regenTime";
	String TAG_BULLET_PREFIX = "bullet";
	String TAG_SELECTED_SLOT = "selectedSlot";

	@Override
	default boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		return slot < 3;
	}

	@Override
	default boolean showSlotInRadialMenu(ItemStack stack, int slot) {
		return isSocketSlotAvailable(stack, slot - 1);
	}

	@Override
	default ItemStack getBulletInSocket(ItemStack stack, int slot) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);

		if (cmp == null)
			return ItemStack.EMPTY;

		return new ItemStack(cmp);
	}

	@Override
	default void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = new NBTTagCompound();

		if (!bullet.isEmpty())
			bullet.writeToNBT(cmp);

		ItemNBTHelper.setCompound(stack, name, cmp);
	}

	@Override
	default int getSelectedSlot(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
	}

	@Override
	default void setSelectedSlot(ItemStack stack, int slot) {
		ItemNBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
	}

	@Override
	default void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack bullet = getBulletInSocket(stack, slot);
		if (!bullet.isEmpty() && bullet.getItem() instanceof ISpellSettable) {
			((ISpellSettable) bullet.getItem()).setSpell(player, bullet, spell);
			setBulletInSocket(stack, slot, bullet);
		}
	}

	default void castOnBlockBreak(ItemStack itemstack, EntityPlayer player) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack playerCad = PsiAPI.getPlayerCAD(player);

		if (!playerCad.isEmpty()) {
			ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
			ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
				context.tool = itemstack;
				context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, false, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue());
			});
		}
	}

	static RayTraceResult raytraceFromEntity(World world, Entity player, boolean stopOnLiquid, double range) {
		float scale = 1.0F;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * scale;
		float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * scale;
		double posX = player.prevPosX + (player.posX - player.prevPosX) * scale;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * scale;
		if (player instanceof EntityPlayer)
			posY += ((EntityPlayer) player).eyeHeight;
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * scale;
		Vec3d rayPos = new Vec3d(posX, posY, posZ);
		float zYaw = -MathHelper.cos(yaw * (float) Math.PI / 180);
		float xYaw = MathHelper.sin(yaw * (float) Math.PI / 180);
		float pitchMod = -MathHelper.cos(pitch * (float) Math.PI / 180);
		float azimuth = -MathHelper.sin(pitch * (float) Math.PI / 180);
		float xLen = xYaw * pitchMod;
		float yLen = zYaw * pitchMod;
		Vec3d end = rayPos.addVector(xLen * range, azimuth * range, yLen * range);
		return world.rayTraceBlocks(rayPos, end, stopOnLiquid);
	}

	static void regen(ItemStack stack, Entity entityIn, boolean isSelected) {
		if(entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && !isSelected) {
			EntityPlayer player = (EntityPlayer) entityIn;
			PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
			int regenTime = ItemNBTHelper.getInt(stack, TAG_REGEN_TIME, 0);

			if(!data.overflowed && regenTime % 80 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
				data.deductPsi(600, 5, true);
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
			ItemNBTHelper.setInt(stack, TAG_REGEN_TIME, regenTime + 1);
		}
	}
}
