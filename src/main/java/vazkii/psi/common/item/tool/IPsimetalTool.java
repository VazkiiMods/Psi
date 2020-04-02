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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.ISpellAcceptor;
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
		CompoundNBT cmp = stack.getOrCreateTag().getCompound(name);

		if (cmp.isEmpty())
			return ItemStack.EMPTY;

		return ItemStack.read(cmp);
	}

	@Override
	default void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = new CompoundNBT();

		if (!bullet.isEmpty())
			cmp = bullet.write(cmp);

		stack.getOrCreateTag().put(name, cmp);
	}

	@Override
	default int getSelectedSlot(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TAG_SELECTED_SLOT);
	}

	@Override
	default void setSelectedSlot(ItemStack stack, int slot) {
		stack.getOrCreateTag().putInt(TAG_SELECTED_SLOT, slot);
	}

	@Override
	default void setSpell(PlayerEntity player, ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack bullet = getBulletInSocket(stack, slot);
		if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(stack, slot, bullet);
		}
	}

	default void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {
		if (!isEnabled(itemstack))
			return;

		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack playerCad = PsiAPI.getPlayerCAD(player);

		if (!playerCad.isEmpty()) {
			ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
			ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
				context.tool = itemstack;
				context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, RayTraceContext.FluidMode.NONE, player.getAttributes().getAttributeInstance(PlayerEntity.REACH_DISTANCE).getValue());
			});
		}
	}

	static boolean isRepairableBy(ItemStack stack) {
		return ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/psimetal")).contains(stack.getItem());
	}

	static BlockRayTraceResult raytraceFromEntity(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode, double range) {
		float f = player.rotationPitch;
		float f1 = player.rotationYaw;
		Vec3d vec3d = player.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
		float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d0 = range; // Botania - use custom range
		Vec3d vec3d1 = vec3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
		return worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
	}

	static void regen(ItemStack stack, Entity entityIn, boolean isSelected) {
		if (entityIn instanceof PlayerEntity && stack.getDamage() > 0 && !isSelected) {
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

	default boolean isEnabled(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage();
	}
}
