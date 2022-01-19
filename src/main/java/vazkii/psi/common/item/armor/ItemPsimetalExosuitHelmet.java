/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

public class ItemPsimetalExosuitHelmet extends ItemPsimetalArmor implements ISensorHoldable, DyeableLeatherItem {

	private static final String TAG_SENSOR = "sensor";

	public ItemPsimetalExosuitHelmet(EquipmentSlot slotType, Item.Properties properties) {
		super(slotType, properties);
	}

	@Override
	public String getEvent(ItemStack stack) {
		ItemStack sensor = getAttachedSensor(stack);
		if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
			return ((IExosuitSensor) sensor.getItem()).getEventType(sensor);
		}

		return super.getEvent(stack);
	}

	@Override
	public int getCastCooldown(ItemStack stack) {
		return 40;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(@Nonnull ItemStack stack) {
		ItemStack sensor = getAttachedSensor(stack);
		if (!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
			return ((IExosuitSensor) sensor.getItem()).getColor(sensor);
		}

		return super.getColor(stack);
	}

	@Override
	public ItemStack getAttachedSensor(ItemStack stack) {
		CompoundTag cmp = stack.getOrCreateTag().getCompound(TAG_SENSOR);
		return ItemStack.of(cmp);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		boolean overlay = type != null && type.equals("overlay");
		return overlay ? LibResources.MODEL_PSIMETAL_EXOSUIT : LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR;
	}

	@Override
	public void attachSensor(ItemStack stack, ItemStack sensor) {
		CompoundTag cmp = new CompoundTag();
		if (!sensor.isEmpty()) {
			sensor.save(cmp);
		}
		stack.getOrCreateTag().put(TAG_SENSOR, cmp);
	}

}
