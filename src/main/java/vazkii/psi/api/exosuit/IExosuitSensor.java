/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.exosuit;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * An Item that implements this counts as a Sensor, and can be put on an Exosuit helmet.
 */
public interface IExosuitSensor {

    String getEventType(ItemStack stack);

    @OnlyIn(Dist.CLIENT)
    int getColor(ItemStack stack);

}
