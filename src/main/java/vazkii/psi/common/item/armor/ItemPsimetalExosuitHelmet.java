/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.armor;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.ISensorHoldable;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibResources;

import javax.annotation.Nonnull;

public class ItemPsimetalExosuitHelmet extends ItemPsimetalArmor implements ISensorHoldable {


    public ItemPsimetalExosuitHelmet(ArmorItem.Type type, Item.Properties properties) {
        super(type, properties);
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
        return new ItemStack(stack.getOrDefault(ModItems.TAG_SENSOR, Items.AIR));
    }

    @Override
    public void attachSensor(ItemStack stack, ItemStack sensor) {
        stack.set(ModItems.TAG_SENSOR, sensor.getItem());
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return layer.dyeable() ? LibResources.MODEL_PSIMETAL_EXOSUIT_SENSOR : LibResources.MODEL_PSIMETAL_EXOSUIT;
    }


}
