/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import vazkii.psi.api.exosuit.ISensorHoldable;

import javax.annotation.Nonnull;

public class SensorRemoveRecipe extends CustomRecipe {

    public static final SimpleCraftingRecipeSerializer<SensorRemoveRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(SensorRemoveRecipe::new);

    public SensorRemoveRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@Nonnull CraftingInput inv, @Nonnull Level world) {
        boolean foundHoldable = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (!foundHoldable && stack.getItem() instanceof ISensorHoldable && !((ISensorHoldable) stack.getItem()).getAttachedSensor(stack).isEmpty()) {
                    foundHoldable = true;
                } else {
                    return false;
                }
            }
        }

        return foundHoldable;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingInput inv, HolderLookup.Provider pRegistries) {
        ItemStack holdableItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                holdableItem = stack;
            }
        }

        ItemStack copy = holdableItem.copy();
        ISensorHoldable holdable = (ISensorHoldable) holdableItem.getItem();
        holdable.attachSensor(copy, ItemStack.EMPTY);

        return copy;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack item = inv.getItem(i);
            if (item.getItem() instanceof ISensorHoldable) {
                list.set(i, ((ISensorHoldable) item.getItem()).getAttachedSensor(item));
                break;
            }
        }

        return list;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

}
