/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.world.item.ItemStack;

/**
 * An item that implements this counts as a CAD component and can be used to
 * create a CAD.
 */
public interface ICADComponent {

    /**
     * Gets the component type of the given stack
     */
    EnumCADComponent getComponentType(ItemStack stack);

    /**
     * Gets the stat value for the respective stat of the stack
     */
    int getCADStatValue(ItemStack stack, EnumCADStat stat);

}
