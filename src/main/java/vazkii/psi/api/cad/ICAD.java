/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api.cad;

import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.item.base.ModItems;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;

/**
 * Base interface for a CAD. You probably shouldn't implement this,
 * unless you absolutely know what you are doing.
 */
public interface ICAD {
    //TODO add Datafixer

    /**
     * Sets the component stack inside the CAD's respective component slot
     */

    static void setComponent(ItemStack stack, ItemStack componentStack) {
        IItemHandler itemHandler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        if (!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent component && itemHandler instanceof ComponentItemHandler componentItemHandler) {
            EnumCADComponent componentType = component.getComponentType(componentStack);
            componentItemHandler.setStackInSlot(componentType.ordinal(), componentStack);
        }
    }

    /**
     * Sets the component in this slot for the CAD.
     */
    default void setCADComponent(ItemStack stack, ItemStack component) {
        setComponent(stack, component);
    }

    /**
     * Gets the component used for this CAD in the given slot.
     */
    ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type);

    /**
     * Gets the value of a given CAD stat.
     */
    int getStatValue(ItemStack stack, EnumCADStat stat);

    /**
     * Gets how much Psi is stored in this CAD's battery.
     */
    int getStoredPsi(ItemStack stack);

    /**
     * Has the CAD regen psi equal to the amount passed in. Will never go above
     * the value of the CAD's OVERFLOW stat.
     */
    void regenPsi(ItemStack stack, int psi);

    /**
     * Consumes psi from the CAD's battery equal to the amount passed in. Returns
     * the remainder that couldn't be consumed. Used to prevent damage.
     */
    int consumePsi(ItemStack stack, int psi);

    /**
     * Gets how many vectors this CAD can store in memory.
     */
    int getMemorySize(ItemStack stack);

    void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException;

    Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException;

    int getTime(ItemStack stack);

    void incrementTime(ItemStack stack);

    /**
     * Gets the color of the spells projected by this CAD. Usually just goes back
     * to ICADColorizer.getColor().
     */
    @OnlyIn(Dist.CLIENT)
    int getSpellColor(ItemStack stack);

    /**
     * Performs crafting around the player using this CAD.
     *
     * @param cad    Stack casting the spell
     * @param entity Player casting the spell
     * @param trick  The trick used to craft
     * @return Whether crafting was successful
     */
    boolean craft(ItemStack cad, Player entity, PieceCraftingTrick trick);

}
