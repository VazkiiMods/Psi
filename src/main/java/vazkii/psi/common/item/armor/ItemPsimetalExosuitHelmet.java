/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class ItemPsimetalExosuitHelmet extends ItemPsimetalArmor implements ISensorHoldable,
		IDyeableArmorItem {

	private static final String TAG_SENSOR = "sensor";

	public ItemPsimetalExosuitHelmet(EquipmentSlotType slotType, Item.Properties properties) {
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
		CompoundNBT cmp = stack.getOrCreateTag().getCompound(TAG_SENSOR);
		return ItemStack.read(cmp);
	}

	@Override
	public void attachSensor(ItemStack stack, ItemStack sensor) {
		CompoundNBT cmp = new CompoundNBT();
		if (!sensor.isEmpty()) {
			sensor.write(cmp);
		}
		stack.getOrCreateTag().put(TAG_SENSOR, cmp);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return !getContainerItem(stack).isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return getAttachedSensor(itemStack);
	}

}
