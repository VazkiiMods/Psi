/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [21/02/2016, 14:13:27 (GMT)]
 */
package vazkii.psi.api.exosuit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An Item that implements this counts as a Sensor, and can be put on an Exosuit helmet.
 */
public interface IExosuitSensor {

	String getEventType(ItemStack stack);

	@OnlyIn(Dist.CLIENT)
	int getColor(ItemStack stack);

}
