/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.tool;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

public interface IPsimetalTool {
	//TODO TheidenHD add Datafixer

	static BlockHitResult raytraceFromEntity(Level worldIn, Player player, ClipContext.Fluid fluidMode, double range) {
		float f = player.getXRot();
		float f1 = player.getYRot();
		Vec3 vec3d = player.getEyePosition(1.0F);
		float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
		float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d0 = range; // Botania - use custom range
		Vec3 vec3d1 = vec3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
		return worldIn.clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.OUTLINE, fluidMode, player));
	}

	static void regen(ItemStack stack, Entity entityIn) {
		if(isItemValidForRegen(stack, entityIn)) {
			Player player = (Player) entityIn;
			PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
			int regenTime = stack.getOrDefault(ModItems.TAG_REGEN_TIME, 0);

			if(!data.overflowed && regenTime % 16 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
				data.deductPsi(150, 0, true);
				stack.setDamageValue(stack.getDamageValue() - 1);
			}
			stack.set(ModItems.TAG_REGEN_TIME, regenTime + 1);
		}
	}

	static boolean isItemValidForRegen(ItemStack stack, Entity entityIn) {
		if(!(entityIn instanceof Player player)) {
			return false;
		}
		return player.getOffhandItem() != stack && player.getMainHandItem() != stack && stack.getDamageValue() > 0;
	}

	static boolean isEnabled(ItemStack stack) {
		return stack.getDamageValue() < stack.getMaxDamage();
	}

	default void castOnBlockBreak(ItemStack itemstack, Player player) {
		if(!isEnabled(itemstack) || PieceTrickBreakBlock.doingHarvestCheck.get()) { //TODO Harvest Check dirty hack, why does this get triggered during TrickBreakBlock?
			return;
		}

		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack playerCad = PsiAPI.getPlayerCAD(player);

		if(!playerCad.isEmpty()) {
			ISocketable sockets = ISocketable.socketable(itemstack);
			ItemStack bullet = sockets.getSelectedBullet();
			ItemCAD.cast(player.getCommandSenderWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
				context.tool = itemstack;
				context.positionBroken = raytraceFromEntity(player.getCommandSenderWorld(), player, ClipContext.Fluid.NONE, player.getAttributes().getValue(Attributes.BLOCK_INTERACTION_RANGE));
			});
		}
	}

	/**
	 * Override and return {@code IPsimetalTool.super.initCapabilities(stack, nbt)}, or your own implementation
	 * of {@link ISocketable}, {@link IPsiBarDisplay} and {@link ISpellAcceptor} caps.
	 */
	default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ToolSocketable(stack, 3);
	}
}
