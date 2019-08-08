/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [20/02/2016, 22:28:56 (GMT)]
 */
package vazkii.psi.common.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemPsimetalExosuitHelmet extends ItemPsimetalArmor implements ISensorHoldable {

	private static final String TAG_SENSOR = "sensor";

	public ItemPsimetalExosuitHelmet() {
		super(LibItemNames.PSIMETAL_EXOSUIT_HELMET, 0, EquipmentSlotType.HEAD);
	}

	@Override
	public String getEvent(ItemStack stack) {
		ItemStack sensor = getAttachedSensor(stack);
		if(!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor)
			return ((IExosuitSensor) sensor.getItem()).getEventType(sensor);

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
		if(!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor)
			return ((IExosuitSensor) sensor.getItem()).getColor(sensor);

		return super.getColor(stack);
	}

	@Override
	public ItemStack getAttachedSensor(ItemStack stack) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_SENSOR, false);
		return new ItemStack(cmp);
	}

	@Override
	public void attachSensor(ItemStack stack, ItemStack sensor) {
		CompoundNBT cmp = new CompoundNBT();
		if(!sensor.isEmpty())
			sensor.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_SENSOR, cmp);
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
