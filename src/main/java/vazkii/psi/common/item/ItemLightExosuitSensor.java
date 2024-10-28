/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class ItemLightExosuitSensor extends ItemExosuitSensor {

    public ItemLightExosuitSensor(Properties properties) {
        super(properties);
    }

    @Override
    public String getEventType(ItemStack stack) {
        return PsiArmorEvent.LOW_LIGHT;
    }

    @Override
    public int getColor(ItemStack stack) {
        return ItemExosuitSensor.lightColor;
    }
}
