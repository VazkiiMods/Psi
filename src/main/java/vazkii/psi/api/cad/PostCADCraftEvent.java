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
import net.neoforged.bus.api.Event;

/**
 * Posted after a CAD is crafted in the assembler.
 * Editing the ItemStack does not and cannot guarantee the output item is changed.
 */
public class PostCADCraftEvent extends Event {

    private final ItemStack cad;
    private final ITileCADAssembler assembler;

    public PostCADCraftEvent(ItemStack cad, ITileCADAssembler assembler) {
        this.cad = cad;
        this.assembler = assembler;
    }

    public ITileCADAssembler getAssembler() {
        return assembler;
    }

    public ItemStack getCad() {
        return cad;
    }
}
