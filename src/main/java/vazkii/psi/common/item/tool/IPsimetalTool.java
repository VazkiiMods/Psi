/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nullable;

public interface IPsimetalTool {

	String TAG_REGEN_TIME = "regenTime";
	String TAG_BULLET_PREFIX = "bullet";
	String TAG_SELECTED_SLOT = "selectedSlot";

	default void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {
		if (!isEnabled(itemstack)) {
			return;
		}

		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack playerCad = PsiAPI.getPlayerCAD(player);

		if (!playerCad.isEmpty()) {
			ISocketable sockets = ISocketable.socketable(itemstack);
			ItemStack bullet = sockets.getSelectedBullet();
			ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
				context.tool = itemstack;
				context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, RayTraceContext.FluidMode.NONE, player.getAttributeManager().getAttributeValue(ForgeMod.REACH_DISTANCE.get()));
			});
		}
	}

	@Deprecated // todo remove in 1.17? Provide the proper tool material in your tools for other materials instead
	static boolean isRepairableBy(ItemStack stack) {
		return stack.getItem() == ModItems.psimetal;
	}

	static BlockRayTraceResult raytraceFromEntity(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode, double range) {
		float f = player.rotationPitch;
		float f1 = player.rotationYaw;
		Vector3d vec3d = player.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
		float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d0 = range; // Botania - use custom range
		Vector3d vec3d1 = vec3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
		return worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
	}

	static void regen(ItemStack stack, Entity entityIn) {
		if (isItemValidForRegen(stack, entityIn)) {
			PlayerEntity player = (PlayerEntity) entityIn;
			PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
			int regenTime = stack.getOrCreateTag().getInt(TAG_REGEN_TIME);

			if (!data.overflowed && regenTime % 16 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
				data.deductPsi(150, 0, true);
				stack.setDamage(stack.getDamage() - 1);
			}
			stack.getOrCreateTag().putInt(TAG_REGEN_TIME, regenTime + 1);
		}
	}

	static boolean isItemValidForRegen(ItemStack stack, Entity entityIn) {
		if (!(entityIn instanceof PlayerEntity)) {
			return false;
		}
		PlayerEntity player = (PlayerEntity) entityIn;
		return player.getHeldItemOffhand() != stack && player.getHeldItemMainhand() != stack && stack.getDamage() > 0;
	}

	default boolean isEnabled(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage();
	}

	/**
	 * Override and return {@code IPsimetalTool.super.initCapabilities(stack, nbt)}, or your own implementation
	 * of {@link ISocketable}, {@link IPsiBarDisplay} and {@link ISpellAcceptor} caps.
	 */
	default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ToolSocketable(stack, 3);
	}
}
