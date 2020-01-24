/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 00:16:37 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;

public class ItemCADBattery extends ItemCADComponent {


    public ItemCADBattery(String name, Item.Properties properties) {
        super(name, properties);
    }


    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.BATTERY;
    }

}
